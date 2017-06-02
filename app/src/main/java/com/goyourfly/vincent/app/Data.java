package com.goyourfly.vincent.app;

import android.os.Environment;

final class Data {
  static final String BASE = "http://i.imgur.com/";
  static final String EXT = ".jpg";
  static final String[] URLS = {
      BASE + "CqmBjo5" + EXT, BASE + "zkaAooq" + EXT, BASE + "0gqnEaY" + EXT,
      BASE + "9gbQ7YR" + EXT, BASE + "aFhEEby" + EXT, BASE + "0E2tgV7" + EXT,
      BASE + "P5JLfjk" + EXT, BASE + "nz67a4F" + EXT, BASE + "dFH34N5" + EXT,
      BASE + "FI49ftb" + EXT, BASE + "DvpvklR" + EXT, BASE + "DNKnbG8" + EXT,
      BASE + "yAdbrLp" + EXT, BASE + "55w5Km7" + EXT, BASE + "NIwNTMR" + EXT,
      BASE + "DAl0KB8" + EXT, BASE + "xZLIYFV" + EXT, BASE + "HvTyeh3" + EXT,
      BASE + "Ig9oHCM" + EXT, BASE + "7GUv9qa" + EXT, BASE + "i5vXmXp" + EXT,
      BASE + "glyvuXg" + EXT, BASE + "u6JF6JZ" + EXT, BASE + "ExwR7ap" + EXT,
      BASE + "Q54zMKT" + EXT, BASE + "9t6hLbm" + EXT, BASE + "F8n3Ic6" + EXT,
      BASE + "P5ZRSvT" + EXT, BASE + "jbemFzr" + EXT, BASE + "8B7haIK" + EXT,
      BASE + "aSeTYQr" + EXT, BASE + "OKvWoTh" + EXT, BASE + "zD3gT4Z" + EXT,
      BASE + "z77CaIt" + EXT,
  };

  static final String[] URLS2 = {
           "http://img1.imgtn.bdimg.com/it/u=4226583571,1837239880&fm=23&gp=0.jpg"
          ,"http://img3.imgtn.bdimg.com/it/u=2382167348,3310707395&fm=23&gp=0.jpg"
          ,"http://img3.imgtn.bdimg.com/it/u=2274840044,1982563105&fm=23&gp=0.jpg"
          ,"http://img5.imgtn.bdimg.com/it/u=1158750344,1797129363&fm=23&gp=0.jpg"
          ,"http://img3.imgtn.bdimg.com/it/u=1012127017,3663630432&fm=26&gp=0.jpg"
          ,"http://img4.imgtn.bdimg.com/it/u=355831296,2192921770&fm=26&gp=0.jpg"
          ,"http://img1.imgtn.bdimg.com/it/u=191561718,3849128437&fm=26&gp=0.jpg"
          ,"http://img0.imgtn.bdimg.com/it/u=3132101816,295436104&fm=26&gp=0.jpg"
          ,"http://img2.imgtn.bdimg.com/it/u=2467829217,1550357573&fm=26&gp=0.jpg"
          ,"http://img3.imgtn.bdimg.com/it/u=3376852328,403026289&fm=26&gp=0.jpg"
          ,"http://img3.imgtn.bdimg.com/it/u=831508631,1640658082&fm=26&gp=0.jpg"
          ,"http://img5.imgtn.bdimg.com/it/u=3412371719,3305800528&fm=26&gp=0.jpg"
          ,"http://img5.imgtn.bdimg.com/it/u=1062104105,1154269956&fm=26&gp=0.jpg"
          ,"http://img3.imgtn.bdimg.com/it/u=4293567271,323282142&fm=26&gp=0.jpg"
          ,"http://img3.imgtn.bdimg.com/it/u=2340966125,1887442018&fm=26&gp=0.jpg"
          ,"http://img4.imgtn.bdimg.com/it/u=199153884,1863045166&fm=26&gp=0.jpg"
          ,"http://img5.imgtn.bdimg.com/it/u=3054120148,775545841&fm=26&gp=0.jpg"
          ,"http://img3.imgtn.bdimg.com/it/u=508171837,445192136&fm=26&gp=0.jpg"
          ,"http://img3.imgtn.bdimg.com/it/u=3236950055,2352554342&fm=26&gp=0.jpg"
          ,"http://img4.imgtn.bdimg.com/it/u=3115709373,2168789801&fm=26&gp=0.jpg"
          ,"http://img2.imgtn.bdimg.com/it/u=464596834,3212821948&fm=26&gp=0.jpg"
          ,"http://img0.imgtn.bdimg.com/it/u=1610279173,3689000373&fm=26&gp=0.jpg"
          ,"http://img5.imgtn.bdimg.com/it/u=3386863036,3670442021&fm=26&gp=0.jpg"
          ,"http://img2.imgtn.bdimg.com/it/u=872461478,1137671160&fm=26&gp=0.jpg"
          ,"http://img3.imgtn.bdimg.com/it/u=3489662932,1022520759&fm=26&gp=0.jpg"
          ,"http://img3.imgtn.bdimg.com/it/u=2797516020,2084961685&fm=26&gp=0.jpg"
          ,"http://img0.imgtn.bdimg.com/it/u=904140604,2360043920&fm=26&gp=0.jpg"
          ,"http://img4.imgtn.bdimg.com/it/u=1963340306,2966897112&fm=26&gp=0.jpg"
          ,"http://img4.imgtn.bdimg.com/it/u=2871638742,3952883960&fm=26&gp=0.jpg"
          ,"http://img3.imgtn.bdimg.com/it/u=3853888603,898333842&fm=26&gp=0.jpg"
          ,"http://img1.imgtn.bdimg.com/it/u=1539153123,2284376056&fm=26&gp=0.jpg"
          ,"http://img5.imgtn.bdimg.com/it/u=2087167640,1710615525&fm=26&gp=0.jpg"
          ,"http://img4.imgtn.bdimg.com/it/u=667632472,455099700&fm=26&gp=0.jpg"
          ,"http://img4.imgtn.bdimg.com/it/u=3811208651,1163989929&fm=26&gp=0.jpg"
          ,"http://img5.imgtn.bdimg.com/it/u=3718944824,1146973771&fm=26&gp=0.jpg"
          ,"http://img4.imgtn.bdimg.com/it/u=2661512177,2671501041&fm=26&gp=0.jpg"
          ,"http://img0.imgtn.bdimg.com/it/u=1602513925,454921121&fm=26&gp=0.jpg"
          ,"http://img3.imgtn.bdimg.com/it/u=2475936457,313943810&fm=26&gp=0.jpg"
          ,"http://img4.imgtn.bdimg.com/it/u=3308202184,1335873496&fm=26&gp=0.jpg"
          ,"http://img2.imgtn.bdimg.com/it/u=790136929,2931538723&fm=26&gp=0.jpg"
          , Environment.getExternalStorageDirectory()+"/Download/1492413389201.jpg"
          , Environment.getExternalStorageDirectory()+"/Download/1.sm.webp"
          , Environment.getExternalStorageDirectory()+"/Download/2.sm.webp"
          ,"https://www.gstatic.com/webp/gallery/1.sm.webp"
          ,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496401030623&di=e2cc31d08700e27dd409bc20222b6bd5&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fzhidao%2Fwh%253D450%252C600%2Fsign%3D33e67285d343ad4ba67b4ec4b7327699%2F574e9258d109b3de76576b48cebf6c81800a4c22.jpg"
  };

  private Data() {
    // No instances.
  }
}