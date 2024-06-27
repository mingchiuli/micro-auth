package org.chiu.micro.gateway.req;

import lombok.Data;

@Data
public class ImgUploadReq {
  
    private byte[] data;

    private String fileName;
}
