/*
 * This file is part of LiteLoader.
 * Copyright (C) 2012-16 Adam Mummery-Smith
 * All Rights Reserved.
 */
package com.mumfrey.liteloader.core;

import com.google.common.io.ByteSink;
import com.google.common.io.Files;
import com.mumfrey.liteloader.launch.ClassPathUtilities;
import com.mumfrey.liteloader.launch.LoaderProperties;
import com.mumfrey.liteloader.update.UpdateSite;
import com.mumfrey.liteloader.util.log.LiteLoaderLogger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LiteLoaderUpdateSite extends UpdateSite {
	private static final String UPDATE_SITE_URL = "http://dl.liteloader.com/versions/";
	private static final String UPDATE_SITE_VERSIONS_JSON = "versions.json";
	private static final String UPDATE_SITE_ARTEFACT_NAME = "com.mumfrey:liteloader";

	private static final Pattern SNAPSHOT_REGEX = Pattern.compile("^([0-9\\._]+)-SNAPSHOT-(r[0-9a-z]+)-(b([0-9]+))-(.*)$", Pattern.CASE_INSENSITIVE);

	private String mcVersion;

	private File mcDir;
	private File jarFile = null;

	private boolean updateForced = false;
	private boolean isSnapshot = false;

	private int currentBuild, availableBuild;
	private String snapshotDate = null;

	public LiteLoaderUpdateSite(String targetVersion, long currentTimeStamp) {
		super(LiteLoaderUpdateSite.UPDATE_SITE_URL, LiteLoaderUpdateSite.UPDATE_SITE_VERSIONS_JSON, targetVersion,
			LiteLoaderUpdateSite.UPDATE_SITE_ARTEFACT_NAME, currentTimeStamp);

		this.mcVersion = targetVersion;
	}

	@Override
	public void beginUpdateCheck() {
		this.isSnapshot = LiteLoader.isSnapshot();
		super.beginUpdateCheck();
	}

	@Override
	public boolean isSnapshot() {
		return this.isSnapshot;
	}

	@Override
	public String getAvailableVersion() {
		if (this.isSnapshot() && this.availableBuild > 0) {
			return String.valueOf(this.availableBuild);
		}

		return super.getAvailableVersion();
	}

	@Override
	public String getAvailableVersionDate() {
		if (this.snapshotDate != null) {
			return this.snapshotDate;
		}

		return super.getAvailableVersionDate();
	}

	@Override
	protected boolean compareArtefact(Map<?, ?> artefact, long bestTimeStamp, Long remoteTimeStamp) {
		if (this.isSnapshot()) {
			String remoteBuild = artefact.get("build").toString();
			String myBuild = LiteLoader.getBranding();

			Matcher remoteMatcher = LiteLoaderUpdateSite.SNAPSHOT_REGEX.matcher(remoteBuild);
			Matcher myMatcher = LiteLoaderUpdateSite.SNAPSHOT_REGEX.matcher(myBuild);

			if (remoteMatcher.matches() && myMatcher.matches()) {
				this.currentBuild = Integer.parseInt(myMatcher.group(4));
				this.availableBuild = Integer.parseInt(remoteMatcher.group(4));
				this.snapshotDate = String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM", new Date(remoteTimeStamp * 1000L));
				return this.availableBuild > this.currentBuild;
			}
		}

		return super.compareArtefact(artefact, bestTimeStamp, remoteTimeStamp);
	}

	public boolean canForceUpdate(LoaderProperties properties) {
		if (!properties.getAndStoreBooleanProperty(LoaderProperties.OPTION_FORCE_UPDATE, false)) {
			return false;
		}

		if (this.hasJarFile()) return true;
		return this.findJarFile();
	}

	/**
	 *
	 */
	private boolean findJarFile() {
		// Find the jar containing liteloader
		File jarFile = ClassPathUtilities.getPathToResource(LiteLoader.class, "/" + LiteLoader.class.getName().replace('.', '/') + ".class");
		if (!jarFile.isFile()) return false;

		// Validate that the jar is in the expected name and location
		this.mcDir = this.walkAndValidateParents(jarFile, "liteloader-" + this.mcVersion + ".jar", this.mcVersion,
			"liteloader", "mumfrey", "com", "libraries");
		if (this.mcDir == null) return false;

		// Check that the jar we found is actually on the current classpath
		if (!ClassPathUtilities.isJarOnClassPath(jarFile)) return false;
		this.jarFile = jarFile;
		return true;
	}

	private File walkAndValidateParents(File file, String... breadcrumbs) {
		try {
			for (String breadcrumb : breadcrumbs) {
				if (file == null || !file.exists() || !file.getName().equals(breadcrumb)) return null;
				file = file.getParentFile();
			}

			return file;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}


	public boolean canCheckForUpdate() {
		return !this.updateForced;
	}

	public boolean hasJarFile() {
		return this.jarFile != null;
	}

	public File getJarFile() {
		return this.jarFile;
	}

	public boolean forceUpdate() {
		if (this.jarFile != null) {
			LiteLoaderLogger.info("Attempting to force update, extracting jar assassin...");

			File jarAssassinOutput = new File(this.jarFile.getParentFile(), "liteloader-update-agent.jar");

			if (!LiteLoaderUpdateSite.extractFile("/update/liteloader-update-agent.jar", jarAssassinOutput) || !jarAssassinOutput.isFile()) {
				LiteLoaderLogger.info("Couldn't extract jarassassin jar, can't force update");
				return false;
			}

			File joptSimple = new File(this.mcDir, "libraries/net/sf/jopt-simple/jopt-simple/4.5/jopt-simple-4.5.jar");

			ProcessBuilder jarAssassinProcBuilder = new ProcessBuilder(
				LiteLoaderUpdateSite.getJavaExecutable().getAbsolutePath(),
				"-cp", joptSimple.getAbsolutePath(),
				"-jar", jarAssassinOutput.getAbsolutePath(),
				"--jarFile", this.jarFile.getAbsolutePath()).directory(this.jarFile.getParentFile());
			try {
				System.err.println(jarAssassinProcBuilder.command());

				@SuppressWarnings("unused")
				Process jarAssassin = jarAssassinProcBuilder.start();

				ClassPathUtilities.deleteClassPathJar(this.jarFile.getAbsolutePath());

				return true;
			} catch (Throwable th) {
				LiteLoaderLogger.info("Couldn't execute jarassassin jar, can't force update");
				return false;
			}
		}

		return false;
	}

	protected static boolean extractFile(String resourceName, File outputFile) {
		try {
			final InputStream inputStream = LiteLoaderUpdateSite.class.getResourceAsStream(resourceName);
			final ByteSink outputSupplier = Files.asByteSink(outputFile);
			outputSupplier.writeFrom(inputStream);
		} catch (NullPointerException ex) {
			return false;
		} catch (IOException ex) {
			return false;
		}

		return true;
	}

	protected static File getJavaExecutable() {
		File javaBin = new File(new File(System.getProperty("java.home")), "bin");
		File javaWin = new File(javaBin, "javaw.exe");
		String osName = System.getProperty("os.name").toLowerCase();
		return osName.contains("win") && javaWin.isFile() ? javaWin : new File(javaBin, "java");
	}
}
