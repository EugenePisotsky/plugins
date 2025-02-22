// Copyright 2018 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.googlemaps;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

/** Receiver of Marker configuration options. */
interface MarkerOptionsSink {
  void setAlpha(float alpha);

  void setAnchor(float u, float v);

  void setConsumeTapEvents(boolean consumeTapEvents);

  void setDraggable(boolean draggable);

  void setFlat(boolean flat);

  void setIcon(BitmapDescriptor bitmapDescriptor);

  void setInfoWindowAnchor(float u, float v);

  void setPositionAnimated(LatLng position, float duration, boolean linear);

  void setAnimatedAnchor(float pu, float pv, float u, float v, float duration);

  void setInfoWindowText(String title, String snippet);

  void setPosition(LatLng position);

  void setRotation(float rotation);

  void setVisible(boolean visible);

  void setZIndex(float zIndex);
}
