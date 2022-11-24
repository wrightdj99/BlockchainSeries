/*
 * Name: Dan Wright
 * Project: Proj D
 * Message: Welcome to Proj D!
 *
 *
 * I got help with the timestamp helper method from this link: https://www.javatpoint.com/java-date-to-timestamp
 *
 * Also, for this project, you don't run any Java commands in the prompt below. Instead, you run the batch
 * file in this directory called danScript.bat. Run this by typing in either ./danScript.bat or just danScript.bat.

 * */

import java.util.*;
import java.io.*;
import java.security.*;
import java.text.*;
//To get the timestamp. I find it odd that java.util wouldn't have something for this in it... only date.
import java.sql.Timestamp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


class BlockRecord{
    String data;
    String randomSeed;
    String prevHash;
    String winningHash;
    UUID uuid;
    String timeStamp;
    String blockID;

    public String getPreviousHash() {return this.prevHash;}
    public void setPreviousHash (String PH){this.prevHash = PH;}
    public String getRandomSeed() {return randomSeed;}
    public void setRandomSeed (String RS){this.randomSeed = RS;}
    public String getData() {return data;}
    public void setData (String data){this.data = data;}
    public String getWinningHash() {return winningHash;}
    public void setWinningHash (String WH){this.winningHash = WH;}
    public void setUuid (UUID ourUUID) { this.uuid = ourUUID; }
    public UUID getUuid() { return uuid; }
    public String getBlockID() { return blockID; }
    public void setBlockID(String blockID) { this.blockID = blockID; }
    public void setTimeStamp(String ts) { this.timeStamp = ts; }
    public String getTimeStamp() { return timeStamp; }

}

public class ProjD {
    //For the purposes of this first mini assignment, we're going to pretend that this unverified blocks
    //ArrayList is a text file with data that is going to be verified through "work" ;) and then added to the
    //verified list once we solve the "puzzle"
    ArrayList<BlockRecord> unverifiedBlocks = new ArrayList<>();
    ArrayList<BlockRecord> verifiedBlocks = new ArrayList<>();

    private static final int indexForData = 0;
    private static final int indexForRandomSeed = 1;
    private static final int indexForPrevHash = 2;
    private static final int indexForWinningHash = 3;
    private static final int indexForUUID = 4;
    private static final int indexForTime = 5;
    private static final int indexForBlockID = 6;



    public static void main(String[] args) {
        System.out.println("Welcome back, this is Dan Wright's Third Blockchain mini assignment!");
        ProjD p = new ProjD();
        if(args.length > 0) {
            System.out.println("Hello from Process " + args[0]);
        }
        p.showOff();
    }

    //This is copied from Dr. Clark Elliott's BlockJ.java helper code.
    //However, to ensure single-responsibility, I gave the randomSeed part of
    //the blockchain it's own method.
    public void createRandom(BlockRecord br){
        Random rr = new Random(); //
        int rval = rr.nextInt(16777215); // This is 0xFFFFFF -- YOU choose what the range is
        rval = rr.nextInt(16777215);
        String randSeed = Integer.toHexString(rval);
        br.setRandomSeed(randSeed);

    }

    public void createUUID(BlockRecord block){
        UUID blockUUID = UUID.randomUUID();
        String returningUUID = blockUUID.toString();
        block.setUuid(blockUUID);
        block.setBlockID(returningUUID);
    }

    public void createTimeStamp(BlockRecord blockRecord){
        Date date = new Date();
        Timestamp blockTime = new Timestamp(date.getTime());
        blockRecord.setTimeStamp(blockTime.toString());
    }

    //Will make a real worksim later. For now, this is Dr. Clark Elliott's simulated work code from BlockJ.java
    public void workSim() throws Exception{
        System.out.println("Working:");
        int randval = 27; // Just some unimportant initial value
        int tenths = 0;
        Random r = new Random();
        for (int i=0; i<1000; i++){ // safety upper limit of 1000
            Thread.sleep(100); // not really work because can be defeated, but OK for our purposes.
            randval = r.nextInt(100); // Higher val = more work
            System.out.print(".");
            if (randval < 4) {       // Lower threshold = more work
                tenths = i;
                break;
            }
        }
    }

    public void serializeBlockchain(ArrayList<BlockRecord> list){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String gsonList = gson.toJson(verifiedBlocks);
        System.out.println("This is what we'll get in our JSON file. We've successfully serialized this blockchain! " +
                " Now, you could potentially hand this serialized JSON file off to a friend who could read it into their PC" +
                " using a similar blockchain program.");
        try(FileWriter serializer = new FileWriter("myList.json")){
            gson.toJson(verifiedBlocks, serializer);
            System.out.println(gsonList);
        } catch(IOException e) { e.printStackTrace();}
    }

    public void retrieveBlockChain(String fileName){
        System.out.println("Reading in from the file: " + fileName + "...");
        String result;
        Gson gson = new Gson();
        try(Reader reader = new FileReader("C:\\Users\\wrigh\\OneDrive\\Desktop\\BlockchainAssignment\\ProjC\\myList.json")){
            BlockRecord[] br = gson.fromJson(reader, BlockRecord[].class);
            for(int i = 0; i < br.length; i++){
                System.out.println("------------------------------------------------------------------------");
                System.out.println("BLOCK NUMBER: " + (i + 1));
                System.out.println("DATA: " + br[i].data);
                System.out.println("RANDOM SEED: " + br[i].randomSeed);
                System.out.println("PREVIOUS HASH: " + br[i].prevHash);
                System.out.println("WINNING HASH: " + br[i].winningHash);
                System.out.println("UUID: " + br[i].uuid);
                System.out.println("TIMESTAMP: " + br[i].timeStamp);
                System.out.println("BLOCK ID: " + br[i].blockID);
                System.out.println("------------------------------------------------------------------------");
            }
        } catch(IOException e){ e.printStackTrace(); }
    }


    public void showOff(){
        BlockRecord genBlock = makeMoreBlocks("GENESIS BLOCK", "I'm the genesis block!", "00000000");
        BlockRecord block1 = makeMoreBlocks("SECOND BLOCK","I'm the second block!", genBlock.getWinningHash());
        BlockRecord block2 = makeMoreBlocks("THIRD BLOCK", "I'm the third block!", block1.getWinningHash());
        BlockRecord block3 = makeMoreBlocks("FOURTH BLOCK", "I'm the fourth and final block! It's nice to meet you!", block2.getWinningHash());
        //Dr. Elliott's GSON builder code is what I'm starting with:
        serializeBlockchain(verifiedBlocks);
        retrieveBlockChain("myList.json");
    }

    public BlockRecord makeMoreBlocks(String blockName, String data, String prevHash){
        BlockRecord blockRecord = new BlockRecord();
        createRandom(blockRecord);
        createUUID(blockRecord);
        createTimeStamp(blockRecord);
        blockRecord.setData(data);
        blockRecord.setPreviousHash(prevHash);
        //We'll be hashing this later on.
        String combinedString = blockRecord.getData() + blockRecord.getPreviousHash() + blockRecord.getRandomSeed();

        String SHA256String = "";
        SHA256String = makeShaString(SHA256String, combinedString);

        System.out.println("HERE IS OUR COMBINED DATA: " + combinedString);
        try {
            workSim();
        } catch(Exception e){}
        System.out.println("HERE IS OUR WINNING STRING: " + SHA256String);
        //Not much of a puzzle here, the SHA 256 string is always the right one
        if(SHA256String.length() >= 15){
            System.out.println("Yay! You solved the puzzle!!!");
            blockRecord.setWinningHash(SHA256String);
            verifiedBlocks.add(blockRecord);
        }
        else{
            System.out.println("Sorry, you didn't solve the puzzle");
        }
        System.out.println("-------------------------------------------------------------");
        System.out.println(blockName + " - INFO:\n");
        System.out.println(blockName + " - DATA: " + blockRecord.getData());
        System.out.println(blockName + " - PREVIOUS HASH: " + blockRecord.getPreviousHash());
        System.out.println(blockName + " - RANDOM SEED: " + blockRecord.getRandomSeed());
        System.out.println(blockName + " - WINNING HASH: " + blockRecord.getWinningHash());
        System.out.println(blockName + " - UUID: " + blockRecord.getUuid());
        System.out.println(blockName + " - BLOCK ID: " + blockRecord.getBlockID());
        System.out.println(blockName + " - BLOCK TIMESTAMP: " + blockRecord.getTimeStamp());
        System.out.println("OUR OVERALL BLOCKCHAIN LIST NOW HAS THIS MANY BLOCKS: " + verifiedBlocks.size());
        System.out.println("-------------------------------------------------------------\n\n");

        return blockRecord;
    }

    /*Using Dr. Clark Elliott's SHA-256 Code for this. More specifically, I will be making the hash digest of the
        combinedString for genBlock. Though the utility code is the same, the difference here between my program and
        Dr. Elliott's (this can be said for many parts of the code) is that my code is more SOLID, and more DRY as well. Making
        a SHA256 encryption string is not this function's job, so I'm not going to add it here. If I were to do that, then we would
        be making a "God Method" (like God Class) as I call it. God Methods can morph into runaway balls of spaghetti code very fast
        in my experience.

        One more note (and then I'll stop, I promise): I PROBABLY could have made this makeShaString method only take in the
        combined string and nothing else. However, then I would not have had an empty string upon which to build then return to
        the same string. All good though I guess. The method does its job and that's all that matters here.*/
    public String makeShaString(String SHA256String, String combinedString){
        try{
            MessageDigest ourMD = MessageDigest.getInstance("SHA-256");
            ourMD.update (combinedString.getBytes());
            byte byteData[] = ourMD.digest();

            // Converting the byte data to hex code...
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            SHA256String = sb.toString(); // Save this as a string so we can work with it.
        }catch(NoSuchAlgorithmException x){};
        return SHA256String;
    }
}

