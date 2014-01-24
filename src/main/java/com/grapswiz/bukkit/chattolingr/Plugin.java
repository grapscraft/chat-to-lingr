package com.grapswiz.bukkit.chattolingr;

import java.io.IOException;
import java.util.concurrent.Future;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public final class Plugin extends JavaPlugin implements  Listener {
	public static String sessionId;
	public static final String WELCOME_MESSAGE = "ゲーム外にチャットもあるのでよかったら参加してね http://lingr.com/room/gracraft";
    public static final String SESSION_CREATE_URL = "http://lingr.com/api/session/create";
    public static final String ROOM_SAY_URL = "http://lingr.com/api/room/say";

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        
        try {
			AccountPropertyLoader loader = new AccountPropertyLoader();
            sessionId = getSessionId(loader.getUser(), loader.getPassword());
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("bukkit-test plugin has been disabled!");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) throws IOException {
        String message = "<" + event.getPlayer().getDisplayName() + "> " + event.getMessage();

        try {
            postMessage(sessionId, message);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
    	Bukkit.broadcastMessage(WELCOME_MESSAGE);
    	
        try {
            postMessage(sessionId, event.getPlayer().getDisplayName() + " がログインしました");
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        try {
            postMessage(sessionId, event.getPlayer().getDisplayName() + " がログアウトしました");
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private String getSessionId(String user, String password) throws Exception {
        AsyncHttpClient client = new AsyncHttpClient();

        Future<Response> future = client.preparePost(SESSION_CREATE_URL)
                .addParameter("user", user)
                .addParameter("password", password)
                .execute();
        Response response = future.get();
        JSONObject obj = (JSONObject)JSONValue.parse(response.getResponseBody());

        client.close();

        return obj.get("session").toString();
    }

    private void postMessage(String sessionId, String message) throws Exception {
        AsyncHttpClient client = new AsyncHttpClient();

        Future<Response> future = client.preparePost(ROOM_SAY_URL)
                .addParameter("session", sessionId)
                .addParameter("room", "gracraft")
                .addParameter("nickname", "gracraft")
                .addParameter("text", message)
                .execute();
        Response response = future.get();

        client.close();
    }
}
