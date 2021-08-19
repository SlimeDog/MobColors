package dev.ratas.mobcolors.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.function.BiConsumer;

import javax.net.ssl.HttpsURLConnection;

import com.google.common.io.Resources;
import com.google.common.net.HttpHeaders;

import dev.ratas.mobcolors.scheduling.abstraction.Scheduler;

public class UpdateChecker {
	private static final int ID = -1; // TODO - use ID
	private static final String SPIGOT_URL = "https://api.spigotmc.org/legacy/update.php?resource=" + ID;

	private final Scheduler scheduler;

	private String currentVersion;

	private final BiConsumer<VersionResponse, String> versionResponse;

	public UpdateChecker(Scheduler scheduler, String currentVersion, BiConsumer<VersionResponse, String> consumer) {
		this.scheduler = scheduler;
		this.currentVersion = currentVersion;
		this.versionResponse = consumer;
	}

	public void check() {
		scheduler.scheduleAsync(() -> {
			try {
				HttpURLConnection httpURLConnection = (HttpsURLConnection) new URL(SPIGOT_URL).openConnection();
				httpURLConnection.setRequestMethod("GET");
				httpURLConnection.setRequestProperty(HttpHeaders.USER_AGENT, "Mozilla/5.0");

				String fetchedVersion = Resources.toString(httpURLConnection.getURL(), Charset.defaultCharset());

				boolean latestVersion = fetchedVersion.equalsIgnoreCase(this.currentVersion);

				scheduler.schedule(() -> this.versionResponse.accept(
						latestVersion ? VersionResponse.LATEST : VersionResponse.FOUND_NEW,
						latestVersion ? this.currentVersion : fetchedVersion));
			} catch (IOException exception) {
				exception.printStackTrace();
				scheduler.schedule(() -> this.versionResponse.accept(VersionResponse.UNAVAILABLE, null));
			}
		});
	}

	public static enum VersionResponse {
		LATEST, FOUND_NEW, UNAVAILABLE
	}

}
