package knight.clubbing.opening;

public record OpeningServiceConfig(
        int maximumPoolSize,
        int minimumIdle,
        long connectionTimeout,
        long idleTimeout,
        long maxLifetime,
        long validationTimeout,
        long leakDetectionThreshold
) {
    public static OpeningServiceConfig defaultConfig() {
        return new OpeningServiceConfig(1, 0, 10000L, 60000L, 600000L, 5000L, 5000L);
    }

    public static OpeningServiceConfig bookmakerConfig() {
        return new OpeningServiceConfig(20, 5, 10000L, 60000L, 600000L, 5000L, 5000L);
    }
}
