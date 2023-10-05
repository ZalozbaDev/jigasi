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
public class CustomTranslationService
        implements TranslationService
{
    /*
     * The URL of the Custom API.
     */
    public final String API_URL_PROP = "org.jitsi.jigasi.transcription.customTranslate.apiurl";
    
    public final String API_KEY_PROP = "org.jitsi.jigasi.transcription.customTranslate.apikey";

    public final String DEFAULT_API_URL = "http://dummy.url/translate";
    
    public final String DEFAULT_API_KEY = "dummyApiKey";
    
    private final String apiUrl;

    private final String apiKey;

    private final Logger logger = Logger.getLogger(CustomTranslationService.class);

    public CustomTranslationService()
    {
        apiUrl = JigasiBundleActivator.getConfigurationService().getString(API_URL_PROP, DEFAULT_API_URL);

        apiKey = JigasiBundleActivator.getConfigurationService().getString(API_KEY_PROP, DEFAULT_API_KEY);
        
        logger.info("Custom translator instanciated!");
        logger.info("Custom translator API URL: " + apiUrl);
        logger.info("Custom translator API KEY: " + apiKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String translate(String sourceText, String sourceLang, String targetLang)
    {
        logger.info("Translate request: '" + sourceText + "' from " + sourceLang + " to " + targetLang);

        return (sourceLang + "|" + targetLang + ": " + sourceText); 
    }
}
