package Reika.DragonAPI.Instantiable.IO;

import java.io.File;
import javax.sound.midi.Sequence;

import Reika.DragonAPI.IO.ReikaMIDIReader;
import Reika.DragonAPI.Instantiable.MusicScore;

public final class MIDIInterface {
    private final Sequence midi;

    public MIDIInterface(File f) {
        midi = ReikaMIDIReader.getMIDIFromFile(f);
    }

    public MIDIInterface(Class root, String path) {
        midi = ReikaMIDIReader.getMIDIFromFile(root, path);
    }

    /*
    /** Returns the note at the given track and time. Args: Track, Time *//*
	public int getNoteAtTrackAndTime(int track, int time) {/*
		int a = ReikaMIDIReader.readMIDI(midi, track, time, 0);
		if (a != 0 && a != -1)
		;//ReikaJavaLibrary.pConsole(time+" @ "+a);
		return a;*//*
		//return ReikaMIDIReader.getMidiNoteAtChannelAndTime(midi, time, track);
		return 0;
	}*/

    public void debug() {
        ReikaMIDIReader.debugMIDI(midi);
    }

    public int getLength() {
        return ReikaMIDIReader.getMidiLength(midi);
    }

    public int[][][] fillToArray() {
        return ReikaMIDIReader.readMIDIFileToArray(midi);
    }

    public MusicScore fillToScore(boolean readPercussion) {
        return ReikaMIDIReader.readMIDIFileToScore(midi, readPercussion);
    }
}
