package fi.csc.kafka.weather.constants;

public interface NetatmoConstants {
	
	public static String NETATMO_REFRESH_TOKEN=System.getenv("NETATMO_REFRESH_TOKEN");
    
    public static String NETATMO_USERNAME=System.getenv("NETATMO_USERNAME");
    
    public static String NETATMO_PASSWORD=System.getenv("NETATMO_PASSWORD");
    
    public static String NETATMO_CLIENT_ID=System.getenv("NETATMO_CLIENT_ID");
    
    public static String NETATMO_CLIENT_SECRET=System.getenv("NETATMO_CLIENT_SECRET");
    
    public static String NETATMO_AUTH_ENDPOINT = "https://api.netatmo.com/oauth2/token";
    
    public static String NETATMO_PUBLIC_WEATHER_ENDPOINT = "https://api.netatmo.com/api/getpublicdata";
}
