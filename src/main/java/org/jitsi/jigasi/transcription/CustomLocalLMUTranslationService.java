/*
 * Jigasi, the JItsi GAteway to SIP.
 *
 * Copyright @ 2018 - present 8x8, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jitsi.jigasi.transcription;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jitsi.jigasi.JigasiBundleActivator;
import org.jitsi.utils.logging.Logger;
import java.io.IOException;


/**
 * Implements a {@link TranslationService} which will use a custom API to translate the given text from one
 * language to another.
 * <p>
 * <a href="https://custom.url">Custom</a>
 * for more information about the custom API
 * @author Daniel Zoba
 */
public class CustomLocalLMUTranslationService
        implements TranslationService
{
    /*
     * Class representing the json response body from the custom translator where 
     * response has status code 200.
     * Used for json-to-POJO conversion with Gson.
     */
    class CustomTranslateResponse
    {
        private String output_annotatedhtml;
    
        private String output_word_count;
    
        private String output_unk_count;
    
        private String output_text;
    
        private String splitted_input_text;
    
        private String model;
    
        private String type;
    
        private String output_html;
    
        private String direction;
      
        public String getTranslatedText()
        {
            return output_text;
        }
    }

    /*
     * The URL of the Custom API.
     */
    public final String API_URL_PROP = "org.jitsi.jigasi.transcription.customTranslate.apiurl";
    
    public final String API_KEY_PROP = "org.jitsi.jigasi.transcription.customTranslate.apikey";

    public final String DEFAULT_API_URL = "http://dummy.url/translate";
    
    public final String DEFAULT_API_KEY = "dummyApiKey";
    
    private final String apiUrl;

    private final String apiKey;

    private final Logger logger = Logger.getLogger(CustomLocalLMUTranslationService.class);

    public CustomLocalLMUTranslationService()
    {
        apiUrl = JigasiBundleActivator.getConfigurationService().getString(API_URL_PROP, DEFAULT_API_URL);

        apiKey = JigasiBundleActivator.getConfigurationService().getString(API_KEY_PROP, DEFAULT_API_KEY);
        
        logger.info("Custom translator instanciated!");
        logger.info("Custom translator API URL: " + apiUrl);
        logger.info("Custom translator API KEY: " + apiKey);
    }

    public CustomLocalLMUTranslationService(String apiUrl, String apiKey)
    {
        // purely for testing
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String translate(String sourceText, String sourceLang, String targetLang)
    {
        logger.info("Translate request: '" + sourceText + "' from " + sourceLang + " to " + targetLang);

        if ((sourceLang.equals("hsb") && targetLang.equals("de"))
            || (sourceLang.equals("de") && targetLang.equals("hsb")))
        {
            String payload = "{"
                .concat("\"direction\": \"" + sourceLang + "_" + targetLang + "\",")
                .concat("\"text\": \"" + sourceText + "\",")
                .concat("\"translationtype\": \"" + "lsf" + "\",")
                .concat("\"warnings\": \"false\"")
                .concat("}");

            logger.info("POST payload: " + payload);
                
            StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);

            HttpResponse response;
            try (CloseableHttpClient httpClient = HttpClientBuilder.create().build())
            {
                HttpPost request = new HttpPost(apiUrl + apiKey);
                request.setEntity(entity);
                request.setHeader("Accept", "application/json");
                request.setHeader("Content-type", "application/json");
                response = httpClient.execute(request);
                String jsonBody = EntityUtils.toString(response.getEntity());
                
                logger.info("POST response: " + jsonBody);
                
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200)
                {
                    logger.error("Custom translator responded with status code " + statusCode + ".");
                    logger.error(jsonBody);
                    return "";
                }
                Gson gson = new GsonBuilder().create();
                CustomTranslateResponse translateResponse = gson.fromJson(jsonBody, CustomTranslateResponse.class);
                return translateResponse.getTranslatedText();
            }
            catch (IOException e)
            {
                logger.error("Error during request to custom translator service.");
                logger.error(e.toString());
                return "";
            }
        }
        else
        {
            return (sourceLang + "|" + targetLang + ": " + sourceText);
        }
    }
    
    public static void main(String[] args)
    {
        System.out.println("Hello world custom translator test! API URL='" + args[0] + "', API KEY='" + args[1] + "'.");
        
        String res;
        
        CustomLocalLMUTranslationService cts = new CustomLocalLMUTranslationService(args[0], args[1]);
        res = cts.translate("Witajće k nam!", "hsb", "de");
        System.out.println("hsb-->de: " + res);
        res = cts.translate("Willkommen in Bautzen!", "de", "hsb");
        System.out.println("de-->hsb: " + res);
    }
}
