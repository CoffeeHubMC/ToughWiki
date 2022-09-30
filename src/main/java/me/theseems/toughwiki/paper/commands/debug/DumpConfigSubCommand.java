package me.theseems.toughwiki.paper.commands.debug;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.theseems.toughwiki.ToughWiki;
import me.theseems.toughwiki.paper.commands.SubCommand;
import me.theseems.toughwiki.utils.TextUtils;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.CommandSender;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class DumpConfigSubCommand implements SubCommand {
    @Override
    public String getLabel() {
        return "dumpconfig";
    }

    @Override
    public String getDescription() {
        return "dumps system configuration";
    }

    @Override
    public String getPermission() {
        return "toughwiki.command.debug.dumpconfig";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String dump;
        try {
            dump = new ObjectMapper(new YAMLFactory())
                    .writeValueAsString(ToughWiki.getToughConfig());
        } catch (JsonProcessingException e) {
            sender.sendMessage(TextUtils.parse("&cFailed to dump config. Check logs for more info."));
            throw new RuntimeException(e);
        }

        try {
            byte[] postData = dump.getBytes(StandardCharsets.UTF_8);

            int postDataLength = postData.length;

            String requestURL = "https://hastebin.com/documents";
            URL url = new URL(requestURL);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Hastebin Java Api");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);

            String response = null;
            DataOutputStream wr;
            try {
                wr = new DataOutputStream(conn.getOutputStream());
                wr.write(postData);
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                response = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response.contains("\"key\"")) {
                response = response.substring(response.indexOf(":") + 2, response.length() - 2);

                response = "https://hastebin.com/" + response;
            } else {
                throw new IOException("Invalid response from hastebin");
            }

            sender.sendMessage(TextUtils.parse("&b&l[Open Hastebin]").clickEvent(ClickEvent.openUrl(response)));
        } catch (IOException e) {
            e.printStackTrace();
            sender.sendMessage(TextUtils
                    .parse("&cFailed to upload dump to hastebin. Try this: ")
                    .append(TextUtils.parse("&c&l[Open VMC]")
                            .clickEvent(ClickEvent.openUrl("https://theseems.ru/vmc?c=%s"
                                    .formatted(URLEncoder.encode(dump, StandardCharsets.UTF_8))))));
        }
    }
}
