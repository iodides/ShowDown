package iodides.showdown.object;

import java.util.ArrayList;

public class ObjEpisodeList {

    ArrayList<ObjEpisode> episodeList = new ArrayList<ObjEpisode>();

	public void add(ObjEpisode episode) {
        episodeList.add(episode);
	}

	public int size() {
		return episodeList.size();
	}

	public ObjEpisode get(int idx) {
        return episodeList.get(idx);
	}

}
