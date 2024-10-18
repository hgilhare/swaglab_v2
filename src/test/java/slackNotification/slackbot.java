package slackNotification;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.auth.AuthTestRequest;
import com.slack.api.methods.request.files.FilesUploadV2Request;
import com.slack.api.methods.response.auth.AuthTestResponse;
import com.slack.api.methods.response.files.FilesUploadV2Response;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class slackbot {
    private static final Logger LOGGER = Logger.getLogger(slackbot.class.getName());
    private static final String SLACK_BOT_TOKEN = System.getenv("SLACK_BOT_TOKEN_1");
    @Test
    public static void slack() {
        System.out.println("Slack Bot Token: " + SLACK_BOT_TOKEN);
        Slack slack = Slack.getInstance();

        try {
            // Test if the token is valid
            AuthTestResponse authTestResponse = slack.methods(SLACK_BOT_TOKEN).authTest(AuthTestRequest.builder().build());
            String botUserId = authTestResponse.getUserId();
            LOGGER.log(Level.INFO, "Bot User ID: " + botUserId);


            // Specify the file to upload
            File file = new File("./target/htmlreport/test.html");// Change the file path accordingly
            List<String> channellist=new ArrayList<>();
            String channelId = "C07D5SG6QRY";// Replace with your channel ID
            channellist.add(channelId);

            // Create the file upload request
            FilesUploadV2Request uploadRequest = FilesUploadV2Request.builder()
                    .channel(channelId).file(file).title("swaglab reports").initialComment("Allure reports").build();

            // Upload the file
            FilesUploadV2Response uploadResponse = slack.methods(SLACK_BOT_TOKEN).filesUploadV2(uploadRequest);

            // Check if the upload was successful
            if (uploadResponse.isOk()) {
                LOGGER.log(Level.INFO, "File uploaded successfully: " + uploadResponse.getFile().getName());
            } else {
                LOGGER.log(Level.SEVERE, "Error uploading file: " + uploadResponse.getError());
            }

        } catch (IOException | SlackApiException e) {
            LOGGER.log(Level.SEVERE, "An error occurred", e);
        }
    }
}
