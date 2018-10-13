package utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import persistance.Device;


public class Utils {

	public static boolean emailValidator(String email) {
		boolean isValid = false;
		try {
			InternetAddress internetAddress = new InternetAddress(email);
			internetAddress.validate();
			isValid = true;
			}
		catch (AddressException e) 
		{
			 isValid = false;
		}
		return isValid;
	}
	public static String tokenGenerator () 
	{
		SecureRandom random = new SecureRandom();
		long longToken = Math.abs( random.nextLong() );
        String result = Long.toString( longToken, 16 );
        return result;
	}
	
	public static String toMD5(String passwordToHash) throws NoSuchAlgorithmException {
		String generatedPassword = null;
     
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passwordToHash.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();

		return generatedPassword;
	}

	public static String getValidationEmail(String path)  
	{
		String email = "<!doctype html>"+
"<html lang='en'>"+
"<head>"+
    "<meta charset='UTF-8'>"+
    "<meta name='viewport'"+
          "content='width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0'>"+
    "<meta http-equiv='X-UA-Compatible' content='ie=edge'>"+
    "<title>Document</title>"+
    "<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css' integrity='sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u' crossorigin='anonymous'>"+
    "<style>"+
        ".qlt-confirmation {"+
           " width: 30%;"+
           " margin: 10px auto;"+
        "}"+

   " </style>"+
"</head>"+
"<body>"+
"<div class='qlt-confirmation'>"+
    "<div class='panel panel-default'>"+
        "<div class='panel-body'>"+
            "<center>"+
                "<img src='https://cdn4.iconfinder.com/data/icons/social-communication/142/open_mail_letter-512.png' style='width:30px; height: 30px;'>"+
                "<p class='desc'>Thank you for signing up!<br>This is a confirmation link.<br><a href='"+path+"' class='btn btn-info' role='button'>VERIFY YOUR ACCOUNT</a></p>"+
            "</center>"+

            "<p class='notice'>Note:<br>Using our <b>social login</b>, you will be ask to add your email address during authentication. This is part of our security policy.</p>"+
        "</div>"+
    "</div>"+
"</div>"+
"</body>"+
"</html>";
   		return email;
	}
	public static String newsignIn(String logoutPath,Device device)  
	{
		String newsignIn=""
				+ "<!DOCTYPE html>"+
"<html lang='en'>"+
"<head>"+
  "  <meta charset='UTF-8'>"+
 "   <title>Title</title>"+
"</head>"+
"<body>"+
"<table width='100%' height='100%' style='min-width:348px' border='0' cellspacing='0' cellpadding='0'>"+
    "<tbody>"+
    "<tr height='32px'></tr>"+
    "<tr align='center'>"+
        "<td width='32px'></td>"+
       " <td>"+
            "<table border='0' cellspacing='0' cellpadding='0' style='max-width:600px'>"+
                "<tbody>"+
                "<tr>"+
                    "<td>"+
                        "<table width='100%' border='0' cellspacing='0' cellpadding='0'>"+
                            "<tbody>"+
                            "<tr>"+
                              "  <td align='right'><img width='32' height='32'"+
                             "                          style='display:block;width:32px;height:32px'"+
                            "                           src='https://ci6.googleusercontent.com/proxy/w8ACgsIEmhjGKodu731Z-VOiYfmXsRM4zd6F_w4_cKQ1JPXF_6Y_hEPR_iJKee33yGZ8zR6o_Q08vuYMKmetfyoGNtMpt1d5CU6z3xA=s0-d-e1-ft#https://www.gstatic.com/accountalerts/email/keyhole.png'"+
                           "                            class='CToWUd'></td>"+
                          "  </tr>"+
                         "   </tbody>"+
                       " </table>"+
                    "</td>"+
                "</tr>"+
                "<tr height='16'></tr>"+
                "<tr>"+
                    "<td>"+
                     "   <table bgcolor='#4184F3' width='100%' border='0' cellspacing='0' cellpadding='0'"+
                             "  style='min-width:332px;max-width:600px;border:1px solid #f0f0f0;border-bottom:0;border-top-left-radius:3px;border-top-right-radius:3px'>"+
                            "<tbody>"+
                            "<tr>"+
                              "  <td height='72px' colspan='3'></td>"+
                           " </tr>"+
                           " <tr>"+
                                "<td width='32px'></td>"+
                               " <td style='font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:24px;color:#ffffff;line-height:1.25;min-width:300px'>"+
                              "      New sign-in from "+device.getBrowser()+" on "+device.getOs()+
                             "   </td>"+
                            "    <td width='32px'></td>"+
                           " </tr>"+
                           " <tr>"+
                                "<td height='18px' colspan='3'></td>"+
                           " </tr>"+
                           " </tbody>"+
                       " </table>"+
                    "</td>"+
                "</tr>"+
               " <tr>"+
                    "<td>"+
                       " <table bgcolor='#FAFAFA' width='100%' border='0' cellspacing='0' cellpadding='0'"+
                              " style='min-width:332px;max-width:600px;border:1px solid #f0f0f0;border-bottom:1px solid #c0c0c0;border-top:0;border-bottom-left-radius:3px;border-bottom-right-radius:3px'>"+
                           " <tbody>"+
                          "  <tr height='16px'>"+
                               " <td width='32px' rowspan='3'></td>"+
                                "<td></td>"+
                                "<td width='32px' rowspan='3'></td>"+
                           " </tr>"+
                           " <tr>"+
                               " <td>"+
                                   " <table style='min-width:300px' border='0' cellspacing='0' cellpadding='0'>"+
                                       " <tbody>"+
                                      "  <tr>"+
                                          "  <td style='font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:13px;color:#202020;line-height:1.5;padding-bottom:4px'>"+
                                             device.getOwner().getUsername()+" ,"+
                                          "  </td>"+
                                      "  </tr>"+
                                       " <tr>"+
                                            "<td style='font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:13px;color:#202020;line-height:1.5;padding:4px 0'>"+
                                                "Your MSF Account <a>"+device.getOwner().getEmail()+"</a> was just used to sign"+
                                                "in from <span style='white-space:nowrap'>"+device.getBrowser()+"</span> on <span"+
                                                 "   style='white-space:nowrap'>"+device.getOs()+"</span>."+
                                                "<table border='0' cellspacing='0' cellpadding='0'"+
                                                     "  style='margin-top:48px;margin-bottom:48px'>"+
                                                    "<tbody>"+
                                                    "<tr valign='middle'>"+
                                                        "<td width='32px'></td>"+
                                                       " <td align='center'><img"+
                                                            "    src='https://ci6.googleusercontent.com/proxy/_ehxLExCa2FPeTKuNVAgMUxyx7YBxMq8-qickdiS6h0GI2UChu_KZURQgNm3-OuvpRjUg26eTgHNny2H1gs6Pzzy81YKOLOVHegzDqMfEMQVAWTuszLuOL68hqTN=s0-d-e1-ft#https://www.gstatic.com/accountalerts/email/anonymous_profile_photo.png'"+
                                                           "     width='48px' height='48px'"+
                                                          "      style='width:48px;height:48px;display:block;border-radius:50%'"+
                                                         "       class='CToWUd'></td>"+
                                                        "<td width='16px'></td>"+
                                                        "<td style='line-height:1.2'><span"+
                                                       "         style='font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:20px;color:#202020'>"+device.getOwner().getUsername()+"</span><br><span"+
                                                      "          style='font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:13px;color:#727272'><a>"+device.getOwner().getEmail()+"</a></span>"+
                                                     "   </td>"+
                                                    "</tr>"+
                                                    "<tr valign='middle'>"+
                                                        "<td width='32px' height='24px'></td>"+
                                                        "<td align='center' height='24px'><img"+
                                                                "src='https://ci4.googleusercontent.com/proxy/3v5djkrQw0eYYuEXwiOUoxXYc3R6bFSVEFNL0C3YbDgAYCp7sUIL4fxyDZ8RADuKX3gZ4_z6bAmrACIqNYpHa95AfUrSskjfkEf4nDXRH7A=s0-d-e1-ft#https://www.gstatic.com/accountalerts/email/down_arrow.png'"+
                                                                "width='4px' height='10px'"+
                                                                "style='width:4px;height:10px;display:block'"+
                                                               " class='CToWUd'></td>"+
                                                    "</tr>"+
                                                    "<tr valign='top'>"+
                                                        "<td width='32px'></td>"+
                                                        "<td align='center'><img"+
                                                                "src='https://ci6.googleusercontent.com/proxy/8-TvqI07V6c6EfMmOpioytN1akb1_MYoQR5JjP9FrOcBKg-A1ob9_8rI-og2hhemR-SKt-PTzEc8LHrxdtQOnK5WmXqFWq16_l4IuCD9uPzGQipSyU_VqCQpBegNZjcIuYnKtg=s0-d-e1-ft#https://www.gstatic.com/accountalerts/devices/windows_laptop_wallpaper_big.png'"+
                                                                "width='48px' height='48px'"+
                                                               " style='width:48px;height:48px;display:block'"+
                                                              "  class='CToWUd'></td>"+
                                                       " <td width='16px'></td>"+
                                                       " <td style='line-height:1.2'><span"+
                                                                "style='font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:16px;color:#202020'>"+device.getOs()+"</span><br><span"+
                                                                "style='font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:13px;color:#727272'>"+device.getLastConnection()+"<br>"+device.getBrowser()+"</span>"+
                                                       " </td>"+
                                                  "  </tr>"+
                                                   " </tbody>"+
                                               " </table>"+
                                               " <b>Dont recognize this activity?</b><br> <a"+
                                                "    href='"+logoutPath+"'"+
                                                "    style='text-decoration:none;color:#4285f4' target='_blank'"+
                                                "    data-saferedirecturl='https://www.google.com/url?hl=en&amp;q=https://accounts.google.com/AccountChooser?Email%3Dalarbi.grine@e-volut.io%26continue%3Dhttps://myaccount.google.com/newdevice/nt/1503875946000?rfn%253D31%2526rfnc%253D1%2526eid%253D7379378223532040134%2526et%253D0%2526asae%253D2&amp;source=gmail&amp;ust=1508258458395000&amp;usg=AFQjCNFBPLop4MGviv7p0aqPL_vDfjv_ag'>"+
                                              "Logout</a>"+
                                        "    </td>"+
                                      "  </tr>"+
                                     "   </tbody>"+
                                  "  </table>"+
                               " </td>"+
                          "  </tr>"+
                          "  <tr height='32px'></tr>"+
                          "  </tbody>"+
                       " </table>"+
                  "  </td>"+
                "</tr>"+
                "<tr height='16'></tr>"+
                "<tr>"+
                "    <td style='max-width:600px;font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:10px;color:#bcbcbc;line-height:1.5'></td>"+
               " </tr>"+
               " <tr>"+
                   " <td>"+
                    "    <table style='font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:10px;color:#666666;line-height:18px;padding-bottom:10px'>"+
                          "  <tbody>"+
                         "   <tr height='6px'></tr>"+
                        "    </tbody>"+
                       " </table>"+
                    "</td>"+
                "</tr>"+
                "</tbody>"+
            "</table>"+
        "</td>"+
        "<td width='32px'></td>"+
    "</tr>"+
    "<tr height='32px'></tr>"+
    "</tbody>"+
"</table>"+
"</body>"+
"</html>";
		
   		return newsignIn ;
	}

}
