package com.infinitydheer.themanager.data.repository.datasource.base;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to manage all general basic operations on Files in app directory and otherwise
 * All file operations in the application is managed by this
 */
public class FileManager {
    public FileManager(){}

    /**
     * Function to write to a file
     * @param file Reference to the file, encapsulates the path of the file
     * @param contents Contents to be written to the reference file
     * @return Returns true if successfully written, else false
     */
    public boolean writeToFile(File file, String contents){
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(contents);
            writer.close();
            return true;
        }catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }



    /**
     * Function which reads a file and puts it into a string
     * @param file Reference of the file to be read
     * @return Returns the content of the file passed in, as {@link String}
     */
    public String readFromFile(File file){
        StringBuilder fileContent=new StringBuilder();
        if(file.exists()){
            String stringLine;
            try{
                FileReader reader=new FileReader(file);
                BufferedReader breader=new BufferedReader(reader);
                while((stringLine=breader.readLine())!=null){
                    fileContent.append(stringLine).append('\n');
                }
                breader.close();
                reader.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return fileContent.toString();
    }

    /**
     * Reads a file and writes each line to a new element in a {@link List}
     * @param file
     * @return
     */
    public List<String> readArrayFromFile(File file){
        List<String> fileContent=new ArrayList<>();
        if(file.exists()){
            String stringLine;
            try{
                FileReader reader=new FileReader(file);
                BufferedReader breader=new BufferedReader(reader);
                while((stringLine=breader.readLine())!=null){
                    fileContent.add(stringLine);
                }
                breader.close();
                reader.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return fileContent;
    }



    /**
     * Utility function to delete all files and directories in a directory
     * @param directory Reference to the directory
     * @return Returns true if directory is cleared, else false
     */
    boolean clearDirectory(File directory){
        boolean re=false;
        if(directory.exists()){
            for(File file:directory.listFiles()){
                re=file.delete();
            }
        }
        return re;
    }

    /**
     * Utility function to delete a single file
     * @param file Reference to the file to be deleted
     * @return True if file is successfully deleted, false otherwise
     */
    public boolean deleteFile(File file){
        return file.delete();
    }
}
