package com.example.notification.controller;

import com.example.notification.model.NotificationRequest;
import com.example.notification.service.NotificationService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class NotificationProducerController {

    @Autowired
    NotificationService notificationService;

    @PostMapping("/notify")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiResponses(value = {
            @ApiResponse(message = "Successfully created", code = 202, response = NotificationResponse.class,
                    examples = @Example(value=@ExampleProperty(mediaType = "application/json", value = "{\n" +
                            "  \"channel\": \"WEB_SOCKET\",\n" +
                            "  \"successfullySent\": true\n" +
                            "}"))),
            @ApiResponse(message = "Sending failed", code = 417, response = NotificationResponse.class,
                    examples = @Example(value=@ExampleProperty(mediaType = "application/json", value = "{\n" +
                    "  \"channel\": \"NONE\",\n" +
                    "  \"successfullySent\": false\n" +
                    "}"))),
            @ApiResponse(message = "Validation failed", code = 422, response = NotificationResponse.class,
            examples = @Example(value=@ExampleProperty(mediaType = "application/json", value = "{\n" +
                    "  \"channel\": \"NONE\",\n" +
                    "  \"successfullySent\": false\n" +
                    "}")))
    })
    public ResponseEntity<NotificationResponse> testMessage (@Valid @RequestBody @ModelAttribute NotificationRequest message, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(NotificationResponse.builder().build());
        }

        var response = notificationService.sendNotification(message);

        return ResponseEntity
                .status(response.isSuccessfullySent() ?
                        HttpStatus.ACCEPTED :
                        HttpStatus.EXPECTATION_FAILED)
                .body(response);
    }

}
