package jbs2011.pa3b;



import java.util.HashMap;



import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {

static private SoundManager _instance;
private static SoundPool mSoundPool;
private static HashMap mSoundPoolMap;
private static AudioManager mAudioManager;
private static Context mContext;

SoundManager()
{


}

/**
* Requests the instance of the Sound Manager and creates it
* if it does not exist.
*
* @return Returns the single instance of the SoundManager
*/
static synchronized public SoundManager getInstance()
{
if (_instance == null)
_instance = new SoundManager();
return _instance;
}

/**
* Initialises the storage for the sounds
*
* @param theContext The Application context
*/
public static void initSounds(Context theContext)
{
mContext = theContext;
mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
mSoundPoolMap = new HashMap();
mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
}

/**
* Add a new Sound to the SoundPool
*
* @param Index - The Sound Index for Retrieval
* @param SoundID - The Android ID for the Sound asset.
*/
public static void addSound(int Index,int SoundID)
{
mSoundPoolMap.put(Index, mSoundPool.load(mContext, SoundID, 1));
}

/**
* Loads the various sound assets
* Currently hardcoded but could easily be changed to be flexible.
*/
public static void loadSounds()
{
mSoundPoolMap.put(1, mSoundPool.load(mContext, R.raw.pop, 1));
mSoundPoolMap.put(1, mSoundPool.load(mContext, R.raw.glass, 2));
//mSoundPoolMap.put(2, mSoundPool.load(mContext, R.raw.laugh1, 1));
}

/**
* Plays a Sound
*
* @param index - The Index of the Sound to be played
* @param speed - The Speed to play not, not currently used but included for compatibility
*/
public static void playSound(int index)
{
float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
Object x=(mSoundPoolMap).get(index);
mSoundPool.play(index, streamVolume, streamVolume, 1, 0,1f);
}

/**
* Stop a Sound
* @param index - index of the sound to be stopped
*/
public static void stopSound(int index)
{
mSoundPool.stop((index));
}

/**
* Deallocates the resources and Instance of SoundManager
*/
public static void cleanup()
{
mSoundPool.release();
mSoundPool = null;
mSoundPoolMap.clear();
mAudioManager.unloadSoundEffects();
_instance = null;

}

}