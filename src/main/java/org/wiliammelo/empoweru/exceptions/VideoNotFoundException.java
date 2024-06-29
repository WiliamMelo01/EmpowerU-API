package org.wiliammelo.empoweru.exceptions;

import org.springframework.http.HttpStatus;

public class VideoNotFoundException extends CustomException{

    public VideoNotFoundException(){
        super("Video not found.", HttpStatus.NOT_FOUND.value());
    }

}
