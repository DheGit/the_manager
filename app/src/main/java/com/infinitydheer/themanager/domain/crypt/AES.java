package com.infinitydheer.themanager.domain.crypt;

public final class AES {

    private static final char[] S_BOX={118, 94, 42, 25, 98, 146, 208, 138, 60, 54, 209, 62, 211, 149, 48, 236, 169, 207, 20, 107, 158, 140, 53, 75, 128, 139, 29, 192, 16, 0, 251, 133, 86, 27, 170, 4, 152, 18, 161, 33, 95, 204, 179, 177, 46, 30, 34, 180, 129, 1, 186, 135, 130, 13, 153, 21, 40, 36, 37, 8, 165, 203, 7, 47, 43, 11, 89, 112, 249, 173, 82, 111, 57, 213, 61, 184, 92, 183, 227, 5, 99, 19, 66, 102, 237, 56, 164, 225, 178, 210, 127, 15, 187, 214, 191, 17, 232, 206, 67, 235, 105, 119, 108, 113, 148, 126, 134, 70, 150, 38, 3, 212, 255, 160, 78, 117, 231, 162, 217, 58, 247, 74, 136, 52, 253, 39, 9, 6, 154, 254, 24, 90, 218, 73, 96, 22, 196, 85, 224, 84, 132, 114, 201, 168, 104, 240, 215, 93, 65, 157, 195, 198, 171, 229, 26, 250, 59, 31, 220, 121, 175, 143, 176, 14, 182, 199, 147, 202, 185, 230, 122, 12, 106, 190, 223, 228, 252, 181, 222, 77, 244, 23, 188, 69, 221, 246, 166, 144, 141, 226, 193, 156, 49, 80, 28, 44, 110, 71, 248, 68, 205, 76, 109, 238, 216, 83, 45, 91, 197, 116, 120, 124, 151, 142, 145, 10, 174, 88, 155, 115, 32, 97, 51, 2, 50, 101, 123, 103, 172, 131, 239, 125, 167, 189, 159, 64, 242, 72, 137, 233, 241, 243, 200, 63, 81, 41, 245, 163, 55, 79, 219, 87, 35, 100, 234, 194};

    private static final char[] INV_S_BOX={29, 49, 223, 110, 35, 79, 127, 62, 59, 126, 215, 65, 171, 53, 163, 91, 28, 95, 37, 81, 18, 55, 135, 181, 130, 3, 154, 33, 194, 26, 45, 157, 220, 39, 46, 252, 57, 58, 109, 125, 56, 245, 2, 64, 195, 206, 44, 63, 14, 192, 224, 222, 123, 22, 9, 248, 85, 72, 119, 156, 8, 74, 11, 243, 235, 148, 82, 98, 199, 183, 107, 197, 237, 133, 121, 23, 201, 179, 114, 249, 193, 244, 70, 205, 139, 137, 32, 251, 217, 66, 131, 207, 76, 147, 1, 40, 134, 221, 4, 80, 253, 225, 83, 227, 144, 100, 172, 19, 102, 202, 196, 71, 67, 103, 141, 219, 209, 115, 0, 101, 210, 159, 170, 226, 211, 231, 105, 90, 24, 48, 52, 229, 140, 31, 106, 51, 122, 238, 7, 25, 21, 188, 213, 161, 187, 214, 5, 166, 104, 13, 108, 212, 36, 54, 128, 218, 191, 149, 20, 234, 113, 38, 117, 247, 86, 60, 186, 232, 143, 16, 34, 152, 228, 69, 216, 160, 162, 43, 88, 42, 47, 177, 164, 77, 75, 168, 50, 92, 182, 233, 173, 94, 27, 190, 255, 150, 136, 208, 151, 165, 242, 142, 167, 61, 41, 200, 97, 17, 6, 10, 89, 12, 111, 73, 93, 146, 204, 118, 132, 250, 158, 184, 178, 174, 138, 87, 189, 78, 175, 153, 169, 116, 96, 239, 254, 99, 15, 84, 203, 230, 145, 240, 236, 241, 180, 246, 185, 120, 198, 68, 155, 30, 176, 124, 129, 112};

    private static final byte[][] MATRIX = {
            {2, 3, 1, 1},
            {1, 2, 3, 1},
            {1, 1, 2, 3},
            {3, 1, 1, 2}
    };

    private static final byte[][] INV_MATRIX = {
            {14, 11, 13, 9},
            {9, 14, 11, 13},
            {13, 9, 14, 11},
            {11, 13, 9, 14}
    };


    private char[][] key, iv;
    private char[][][] roundKeys;

    //TODO Test all the methods below this line. After testing, shift the methods above this line

    private static char[][][] keyGen(byte[][] firstKey){
        byte[][] curKey, prevKey;
        curKey=firstKey;

        char[][][] oup=new char[10][4][4];
//      TODO Complete this
        return null;
    }

    private static byte[][] mixColumns(byte[][] inp){
        byte[][] oup=new byte[4][4];

        for(int col=0;col<4;col++){

        }
        return oup;
    }

    private static byte vectorMultiply(byte[] v1, byte[] v2){
        return -1;
    }

    private static byte galoisMultiply(byte number, byte multiplier){

        return -1;
    }

    private static byte[][] subBytes(byte[][] inp, boolean invert){
       char[] map=invert?INV_S_BOX:S_BOX;

       byte[][] oup=new byte[4][4];
       for(int col=0;col<4;col++){
           for(int row=0;row<4;row++){
               int val=byteToInt(inp[col][row]);
               oup[col][row]=(byte)map[val];
           }
       }
       return oup;
    }

    private static byte[][] shiftRows(byte[][] inp) {
        return new byte[][]{
                {inp[1][0],inp[2][0],inp[3][0],inp[0][0]},
                {inp[2][1],inp[3][1],inp[0][1],inp[1][1]},
                {inp[3][2],inp[0][2],inp[1][2],inp[2][2]},
                {inp[0][3],inp[1][3],inp[2][3],inp[3][3]}
        };
    }

    private static byte[][] invShiftRows(byte[][] inp){
        return new byte[][]{
                {inp[3][0],inp[0][0],inp[1][0],inp[2][0]},
                {inp[2][1],inp[3][1],inp[0][1],inp[1][1]},
                {inp[1][2],inp[2][2],inp[3][2],inp[0][2]},
                {inp[0][3],inp[1][3],inp[2][3],inp[3][3]}
        };
    }

    private static int byteToInt(byte x){
        int oup;
        if(x<0) oup=256+x;
        else oup=x;
        return oup;
    }


    //TODO Remove the methods below this line when testing is complete

    public static char[] getsBox(){
        return S_BOX;
    }

    public static char[] getInvSBox(){
        return INV_S_BOX;
    }
}
