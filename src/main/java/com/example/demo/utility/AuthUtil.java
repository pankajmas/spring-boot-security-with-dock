package com.example.demo.utility;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

	private static final Map<String, Integer> failedLoginAttempts = new ConcurrentHashMap<>();
	private static final Map<String, LocalDateTime> lockoutTimestamps = new ConcurrentHashMap<>();

	public void handleAuthenticationFailure(String username) {

		// Implement logic to track and increment the number of failed login attempts
		int failedAttempts = incrementFailedAttempts(username);

		// Check if the user has reached the maximum number of allowed failed attempts
		if (failedAttempts >= 5) {
			// Set a timestamp to indicate when the lockout will be lifted (e.g., 20 minutes
			// from now)
			setLockoutTimestamp(username, LocalDateTime.now().plusMinutes(20));
		}
	}

	public int incrementFailedAttempts(String username) {
		failedLoginAttempts.put(username, failedLoginAttempts.getOrDefault(username, 0) + 1);
		return failedLoginAttempts.get(username);
	}

	public boolean isUserTemporarilyLocked(String username) {
		LocalDateTime lockoutTimestamp = lockoutTimestamps.get(username);
		return lockoutTimestamp != null && LocalDateTime.now().isBefore(lockoutTimestamp);

	}

	public void resetLoginAttempts(String username) {
		failedLoginAttempts.remove(username);
	}

	public void setLockoutTimestamp(String username, LocalDateTime lockoutTimestamp) {
		lockoutTimestamps.put(username, lockoutTimestamp);
	}

	public int getRemainingLoginAttempts(String username) {
		int failedAttempts = failedLoginAttempts.getOrDefault(username, 0);
		return Math.max(0, 5 - failedAttempts); // Assuming 5 total attempts allowed
	}

	public void resetLoginAttempts1(String username) {
		failedLoginAttempts.remove(username);
	}

	public void setLockoutTimestamp1(String username, LocalDateTime lockoutTimestamp) {
		lockoutTimestamps.put(username, lockoutTimestamp);
	}

}
