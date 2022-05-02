package com.example.app_footprint.Email;

import java.util.Random;

public class GenerateCode {
    String code;
    int length;
    char cha[]={'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    public GenerateCode(int x)
    {
         code = "";
        length = x;
    }
    public String generateCode()
    {
        Random ran=new Random();
        for(int i=0;i<length;i++)
        {
            int judge = ran.nextInt(2);
            if(judge==1)
            {

                code += Character.toString(cha[ran.nextInt(26)]);
            }
            else
            {
                code+= Integer.toString(ran.nextInt(10));
            }
        }
        return code;
    }
}
