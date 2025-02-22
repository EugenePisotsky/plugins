// Copyright 2018 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.googlemaps;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/** Controller of a single Marker on the map. */
class MarkerController implements MarkerOptionsSink {

  private final Marker marker;
  private final String googleMapsMarkerId;
  private boolean consumeTapEvents;

  MarkerController(Marker marker, boolean consumeTapEvents) {
    this.marker = marker;
    this.consumeTapEvents = consumeTapEvents;
    this.googleMapsMarkerId = marker.getId();
  }

  void remove() {
    marker.remove();
  }

  @Override
  public void setAlpha(float alpha) {
    marker.setAlpha(alpha);
  }

  @Override
  public void setAnchor(float u, float v) {
    marker.setAnchor(u, v);
  }

  @Override
  public void setConsumeTapEvents(boolean consumeTapEvents) {
    this.consumeTapEvents = consumeTapEvents;
  }

  @Override
  public void setDraggable(boolean draggable) {
    marker.setDraggable(draggable);
  }

  @Override
  public void setFlat(boolean flat) {
    marker.setFlat(flat);
  }

  @Override
  public void setIcon(BitmapDescriptor bitmapDescriptor) {
    marker.setIcon(bitmapDescriptor);
  }

  @Override
  public void setInfoWindowAnchor(float u, float v) {
    marker.setInfoWindowAnchor(u, v);
  }

  @Override
  public void setAnimatedAnchor(final float pu, final float pv, final float u, final float v, final float duration) {
    ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
    valueAnimator.setDuration((long) (duration * 1000));
    valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        try {
          float fr = animation.getAnimatedFraction();

          float nextU = (u - pu) * fr + pu;
          float nextV = (v - pv) * fr + pv;

          marker.setAnchor(nextU, nextV);
        } catch (Exception ex) {
          //I don't care atm..
        }
      }
    });
    valueAnimator.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
      }
    });
    valueAnimator.start();
  }

  @Override
  public void setInfoWindowText(String title, String snippet) {
    marker.setTitle(title);
    marker.setSnippet(snippet);
  }

  @Override
  public void setPosition(LatLng position) {
    marker.setPosition(position);
  }

  @Override
  public void setPositionAnimated(LatLng position, float duration, boolean linear) {
    final LatLng startPosition = marker.getPosition();
    final LatLng endPosition = position;

    final LatLngInterpolatorNew latLngInterpolator = new LatLngInterpolatorNew.LinearFixed();

    ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
    valueAnimator.setDuration((long) (duration * 1000));
    valueAnimator.setInterpolator(linear ? new LinearInterpolator() : new AccelerateDecelerateInterpolator());
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        try {
          float v = animation.getAnimatedFraction();
          LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
          marker.setPosition(newPosition);
        } catch (Exception ex) {
          //I don't care atm..
        }
      }
    });
    valueAnimator.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
      }
    });
    valueAnimator.start();
  }

  @Override
  public void setRotation(float rotation) {
    marker.setRotation(rotation);
  }

  @Override
  public void setVisible(boolean visible) {
    marker.setVisible(visible);
  }

  @Override
  public void setZIndex(float zIndex) {
    marker.setZIndex(zIndex);
  }

  String getGoogleMapsMarkerId() {
    return googleMapsMarkerId;
  }

  boolean consumeTapEvents() {
    return consumeTapEvents;
  }

  private interface LatLngInterpolatorNew {
    LatLng interpolate(float fraction, LatLng a, LatLng b);

    class LinearFixed implements LatLngInterpolatorNew {
      @Override
      public LatLng interpolate(float fraction, LatLng a, LatLng b) {
        double lat = (b.latitude - a.latitude) * fraction + a.latitude;
        double lngDelta = b.longitude - a.longitude;
        // Take the shortest path across the 180th meridian.
        if (Math.abs(lngDelta) > 180) {
          lngDelta -= Math.signum(lngDelta) * 360;
        }
        double lng = lngDelta * fraction + a.longitude;
        return new LatLng(lat, lng);
      }
    }
  }
}
