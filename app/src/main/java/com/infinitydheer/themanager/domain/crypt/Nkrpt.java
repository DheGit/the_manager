package com.infinitydheer.themanager.domain.crypt;

import com.infinitydheer.themanager.domain.data.ConvDomain;
import com.infinitydheer.themanager.domain.data.RazgoDomain;
import com.infinitydheer.themanager.domain.data.UserDomain;

public final class Nkrpt {
    public static final int DEFK=97;

    private static final char[] MAP ={' ','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
    'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
    '0','1','2','3','4','5','6','7','8','9'};

    private static final int[] KMAP ={0,18,31,21,57,17,2,50,12,10,14,58,45,25,41,51,60,11,47,37,34,61,6,3,33,28,13,20,56,54,52,
    27,22,24,59,43,46,40,26,9,49,36,16,23,32,19,4,15,38,8,55,42,30,39,62,35,53,1,48,29,44,5,7};

    private static final int[] INV_KMAP ={0,57,6,23,46,61,22,62,49,39,9,17,8,26,10,47,42,5,1,45,27,3,32,43,33,13,38,31,25,59,52,2,
    44,24,20,55,41,19,48,53,37,14,51,35,60,12,36,18,58,40,7,15,30,56,29,50,28,4,11,34,16,21,54};

    private static final char[] spSubs={'`',';','^'};

    public static String processDef(String inp){
        return processString(inp,DEFK);
    }

    public static String unprocessDef(String inp){
        if(inp==null) return "";
        if(inp.equals("")) return "";
        return unProcessString(inp,DEFK);
    }

    public static RazgoDomain process(RazgoDomain inp){
        int k=createK(inp.getDatetime());
        RazgoDomain oup=new RazgoDomain();
        oup.setK(k);
        oup.setContent(processString(inp.getContent(), k));
        oup.setSender(processString(inp.getSender(),DEFK));
        oup.setDatetime(inp.getDatetime());
        oup.setId(inp.getId());
        oup.setSent(inp.isSent());
        return oup;
    }

    public static RazgoDomain unprocess(RazgoDomain inp){
        int k=inp.getK();
        RazgoDomain oup=new RazgoDomain();

        oup.setK(k);
        oup.setDatetime(inp.getDatetime());
        oup.setId(inp.getId());
        oup.setSent(inp.isSent());

        oup.setContent(unProcessString(inp.getContent(), k));
        oup.setSender(unProcessString(inp.getSender(),DEFK));

        return oup;
    }

    public static ConvDomain unprocess(ConvDomain inp){
        int k=inp.getK();
        ConvDomain oup=new ConvDomain();

        oup.setK(k);
        oup.setConvid(inp.getConvid());
        oup.setLastTime(inp.getLastTime());

        oup.setLastMsg(unProcessString(inp.getLastMsg(),k));
        oup.setLastSender(unprocessDef(inp.getLastSender()));
        oup.setPartnerName(unprocessDef(inp.getPartnerName()));

        return oup;
    }

    public static UserDomain unprocess(UserDomain inp){
        String userId=unprocessDef(inp.getUserId());
        String pass=unprocessDef(inp.getUserPass());

        return new UserDomain(userId,pass);
    }

    private static String processString(String string, int k){
        StringBuilder builder=new StringBuilder();
        int cou=0;
        for(int i=0;i<string.length();i++){
            char curChar=string.charAt(i);
            if(validChar(curChar)){
                builder.append(processChar(curChar, k));
            }else if(curChar== MAP[0]){
                builder.append(spSubs[cou]);
                cou=(cou+1)%3;
            }else{
                builder.append(curChar);
            }
        }
        return builder.toString();
    }

    private static String unProcessString(String string, int k){
        if(string==null) return "";
        StringBuilder builder=new StringBuilder();
        for(int i=0;i<string.length();i++){
            char cure=string.charAt(i);
            if(validChar(cure)){
                builder.append(unprocessChar(cure, k));
            }else if(unallowedChar(cure)){
                builder.append(MAP[0]);
            } else{
                builder.append(string.charAt(i));
            }
        }
        return builder.toString();
    }

    private static char processChar(char x, int k){
        char res=scissor(x, k);
        res=mapChar(res);
        res=scissor(res, 5*k);
        res=mapChar(res);
        res=scissor(res, k);
        res=mapChar(res);
        res=scissor(res, 2*k);
        return res;
    }

    private static char unprocessChar(char x, int k){
        char res=scissor(x, (-2)*k);
        res=rmapChar(res);
        res=scissor(res, -k);
        res=rmapChar(res);
        res=scissor(res,(-5)*k);
        res=rmapChar(res);
        res=scissor(res, -k);
        return res;
    }

    private static char mapChar(char c){
        return MAP[KMAP[charToInt(c)]];
    }

    private static char rmapChar(char c){
        return MAP[INV_KMAP[charToInt(c)]];
    }

    //To be tested
    private static char scissor(char c, int step){
        if(validChar(c)){
            int x=charToInt(c);
            if(x>=1&&x<=26){
                step%=26;
                if(step<0) step+=26;
                x=(x+step)%26;
                if(x==0) x=26;
                return MAP[x];
            }else if(x>=27&&x<=52){
                step%=26;
                if(step<0) step+=26;
                x-=26;
                x=(x+step)%26;
                if(x==0) x=26;
                x+=26;
                return MAP[x];
            }else{
                step%=10;
                if(step<0) step+=10;
                x-=53;
                x=(x+step)%10;
                x+=53;
                return MAP[x];
            }
        }else{
            return c;
        }
    }

    private static int charToInt(char c){
        if(c>='0'&&c<='9') return c+5;
        else if(c>='A'&&c<='Z') return c-38;
        else if(c>='a'&&c<='z') return c-96;
        else return 0;
    }

    private static boolean validChar(char c){
        if(c>='0'&&c<='9') return true;
        else if(c>='a'&&c<='z') return true;
        else return c >= 'A' && c <= 'Z';
    }

    private static boolean unallowedChar(char c){
        return c=='`'||c=='^'||c==';';
    }

    private static int createK(String inp){
        int x=convertToInt(inp.substring(0,2));
        x+=convertToInt(inp.substring(3,5));
        x+=convertToInt(inp.substring(6,10));
        x+=convertToInt(inp.substring(11,13));
        x+=convertToInt(inp.substring(14,16));
        x%=102;
        if(x==0) x=103;
        return x;
    }

    private static int convertToInt(String inp){
        int res=0;
        for(int i=0;i<inp.length();i++){
            if(inp.charAt(i)>='0'&&inp.charAt(i)<='9') res=res*10+(Character.getNumericValue(inp.charAt(i)));
        }
        return res;
    }
}
