package com.frami.utils.extensions

import com.frami.BuildConfig


fun Throwable?.printErrorTrace() {
  if (BuildConfig.DEBUG) {
    this?.printStackTrace()
  }
}