    package org.wiliammelo.empoweru.exceptions;

    import lombok.Getter;
    import lombok.Setter;

    import java.sql.Timestamp;

    @Getter
    public class CustomException extends Exception {

        private Integer error;
        private String message;
        private Timestamp timestamp;

        public CustomException(String message, Integer error){
            super(message);
            this.message = message;
            this.error = error;
            this.timestamp = new Timestamp(System.currentTimeMillis());
        }
    }
