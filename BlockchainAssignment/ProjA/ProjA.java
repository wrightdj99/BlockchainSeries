/*
* Name: Dan Wright
* Project: Proj A
* Message: Welcome to Proj A!
*
*
* I got help with the timestamp helper method from this link: https://www.javatpoint.com/java-date-to-timestamp
* */

import java.util.*;
import java.io.*;
import java.security.*;
import java.text.*;
//To get the timestamp. I find it odd that java.util wouldn't have something for this in it... only date.
import java.sql.Timestamp;

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

public class ProjA {
    //For the purposes of this first mini assignment, we're going to pretend that this unverified blocks
    //ArrayList is a text file with data that is going to be verified through "work" ;) and then added to the
    //verified list once we solve the "puzzle"
    ArrayList<BlockRecord> unverifiedBlocks = new ArrayList<>();
    ArrayList<BlockRecord> verifiedBlocks = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Hey everyone, this is Dan Wright's First Blockchain mini assignment!");
        ProjA p = new ProjA();
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


    public void showOff(){
        /*Usually, the first block in a blockchain is called the "Genesis Block". Let's make that here! :D*/
        BlockRecord genBlock = new BlockRecord();
        genBlock.setData("Hello, I'm the genesis block");
        genBlock.setPreviousHash("00000000");
        createRandom(genBlock);
        createUUID(genBlock);
        createTimeStamp(genBlock);
        System.out.println("Our UUID For the genesis block: " + genBlock.getUuid());
        System.out.println("Our block ID for the genesis block is: " + genBlock.getBlockID());
        System.out.println("This is the timestamp for our genesis block: " + genBlock.getTimeStamp());

        //We'll be hashing this later...

        String combinedString = genBlock.getData() + genBlock.getPreviousHash() + genBlock.getRandomSeed();

        /*Using Dr. Clark Elliott's SHA-256 Code for this. More specifically, I will be making the hash digest of the
        combinedString for genBlock. Though the utility code is the same, the difference here between my program and
        Dr. Elliott's (this can be said for many parts of the code) is that my code is more SOLID, and more DRY as well. Making
        a SHA256 encryption string is not this function's job, so I'm not going to add it here. If I were to do that, then we would
        be making a "God Method" (like God Class) as I call it. God Methods can morph into runaway balls of spaghetti code very fast
        in my experience.

        One more note (and then I'll stop, I promise): I PROBABLY could have made this makeShaString method only take in the
        combined string and nothing else. However, then I would not have had an empty string upon which to build then return to
        the same string. All good though I guess. The method does its job and that's all that matters here.*/
        String SHA256String = "";
        SHA256String = makeShaString(SHA256String, combinedString);

        System.out.println("-------------------------------------------------------------");
        System.out.println("HERE IS OUR COMBINED DATA: " + combinedString);
        System.out.println("-------------------------------------------------------------\n\n");

        try {
            workSim();
        } catch(Exception e){}
        System.out.println("HERE IS OUR WINNING STRING: " + SHA256String);
        //Not much of a puzzle here, the SHA 256 string is always the right one for this first block.
        genBlock.setWinningHash(SHA256String);
        System.out.println("-------------------------------------------------------------");
        System.out.println("GEN BLOCK INFO:\n");
        System.out.println("GEN BLOCK DATA: " + genBlock.getData());
        System.out.println("GEN BLOCK PREVIOUS HASH: " + genBlock.getPreviousHash());
        System.out.println("GEN BLOCK RANDOM SEED: " + genBlock.getRandomSeed());
        System.out.println("GEN BLOCK WINNING HASH: " + genBlock.getWinningHash());
        System.out.println("-------------------------------------------------------------\n\n");

        System.out.println("Adding to verified blocks...");
        verifiedBlocks.add(genBlock);

        BlockRecord block1 = makeMoreBlocks("SECOND BLOCK","I'm the second block!", genBlock.getWinningHash());
        BlockRecord block2 = makeMoreBlocks("THIRD BLOCK", "I'm the third block!", block1.getWinningHash());
        BlockRecord block3 = makeMoreBlocks("FOURTH BLOCK", "I'm the fourth and final block! It's nice to meet you!", block2.getWinningHash());
    }

    public BlockRecord makeMoreBlocks(String blockName, String data, String prevHash){
        BlockRecord blockRecord = new BlockRecord();
        createRandom(blockRecord);
        createUUID(blockRecord);
        createTimeStamp(blockRecord);
        blockRecord.setData(data);
        blockRecord.setPreviousHash(prevHash);
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
        System.out.println("-------------------------------------------------------------\n\n");

        return blockRecord;
    }

    public String makeShaString(String SHA256String, String combinedString){
        try{
            MessageDigest ourMD = MessageDigest.getInstance("SHA-256");
            ourMD.update (combinedString.getBytes());
            byte byteData[] = ourMD.digest();

            // CDE: Convert the byte[] to hex format. THIS IS NOT VERFIED CODE:
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            SHA256String = sb.toString(); // For ease of looking at it, we'll save it as a string.
        }catch(NoSuchAlgorithmException x){};
        return SHA256String;
    }
}