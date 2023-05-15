package com.mobit;

import java.util.List;

public interface IMap {

	List<IMapMarker> getMarkerList();
	void setMarkerList(List<IMapMarker> markerList);
	
	List<IMapMarker> getSelectedMarkerList();
	void setSelectedMarkerList(List<IMapMarker> markerList);
}
