package com.java25.java25.springboot4.mysql.controllers.userAccess;

import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
//import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java25.java25.springboot4.mysql.dto.MfaActivateDto;
import com.java25.java25.springboot4.mysql.entities.User;
import com.java25.java25.springboot4.mysql.services.TwoFactorAuthService;
import com.java25.java25.springboot4.mysql.services.UserService;

@RestController
@RequestMapping("/api")
public class ActivateMfa {
	
	private final UserService userService;
	private final TwoFactorAuthService twoFactorAuthService;
	
	@Autowired
	public ActivateMfa(
			TwoFactorAuthService twoFactorAuthService,
			UserService userService) {
		this.userService = userService;
		this.twoFactorAuthService = twoFactorAuthService;
	}
		
    @PatchMapping(path="/mfa/activate/{id}")	
	public ResponseEntity<Map<String, String>> mfaActivate(@RequestBody MfaActivateDto mfaDto, @PathVariable Integer id) {    	
    	
		User user = userService.getUser(id);
		if (user != null) {
			
	    	if (mfaDto.isTwoFactorEnabled()) {
	    		
	    		String issuer= "BARCLAYS BANK";
	    		String acctName =user.getEmail();
	    		String secret = twoFactorAuthService.generateSecretKey();
	    		String dataUri = twoFactorAuthService.getQRCodeUrl(secret, acctName, issuer);
	    		
	    		String base64String = "";
				try {
					base64String = generateQRBase64(dataUri);
				} catch (Exception e) {
					e.printStackTrace();
				}
	    		
				userService.enableMfa(id, secret, base64String);
	    		HashMap<String, String> map = new HashMap<>();
		        map.put("message", "Qrcode Base64 has been generated, please scan using Mobile Google or Microsoft Authenticator");
		        map.put("qrcodeurl", base64String);
		        	
		        return new ResponseEntity<>(map, HttpStatus.OK);	    		
	    			    				    		
	    	} else {
	    		
	    		userService.disableMfa(id);
	            HashMap<String, String> map = new HashMap<>();
	            map.put("message", "Multi-Factor Authenticator has been disabled.");        	
	            return new ResponseEntity<>(map, HttpStatus.CONFLICT);             		    		
	    		
	    	}			
			
		} else {

            HashMap<String, String> map = new HashMap<>();
            map.put("id", id.toString());
            map.put("message", "User ID not found.");        	
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);             		    					
		}    	    	
    	
	}
	
    public String generateQRBase64(String otpAuthUri) throws Exception {
        if (otpAuthUri == null || otpAuthUri.isBlank()) {
            throw new IllegalArgumentException("URI cannot be empty");
        }

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthUri, BarcodeFormat.QR_CODE, 200, 200);

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "PNG", outputStream);
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());
        }        
    }           
}