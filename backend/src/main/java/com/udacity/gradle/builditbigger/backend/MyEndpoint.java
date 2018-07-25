package com.udacity.gradle.builditbigger.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import hu.drorszagkriszaxel.jokeprovider.JokeProvider;

/** An endpoint class we are exposing */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.builditbigger.gradle.udacity.com",
                ownerName = "backend.builditbigger.gradle.udacity.com",
                packagePath = ""
        )

)
public class MyEndpoint {

    @ApiMethod(name = "tellJoke")
    public MyBean tellJoke() {

        MyBean response = new MyBean();
        response.setData(JokeProvider.tellMeJoke());

        return response;

    }

}
