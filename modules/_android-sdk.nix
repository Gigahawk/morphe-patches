{ pkgs, ... }:
let
  includeAuto = pkgs.stdenv.hostPlatform.isx86_64 || pkgs.stdenv.hostPlatform.isDarwin;
  ndkVersions = [
    "latest"
  ];
in
rec {

  androidComposition = pkgs.androidenv.composeAndroidPackages {
    buildToolsVersions = [ "36.0.0" ];
    includeSources = true;
    includeSystemImages = false;
    includeEmulator = "if-supported";
    includeNDK = "if-supported";
    inherit ndkVersions;
    useGoogleAPIs = true;
    useGoogleTVAddOns = true;

    numLatestPlatformVersions = 10;

    includeExtras = [
      "extras;google;gcm"
    ]
    ++ pkgs.lib.optionals includeAuto [
      "extras;google;auto"
    ];

    extraLicenses = [
      # Already accepted for you with the global accept_license = true or
      # licenseAccepted = true on androidenv.
      # "android-sdk-license"

      # These aren't, but are useful for more uncommon setups.
      "android-sdk-preview-license"
      "android-googletv-license"
      "android-sdk-arm-dbt-license"
      "google-gdk-license"
      "intel-android-extra-license"
      "intel-android-sysimage-license"
      "mips-android-sysimage-license"
    ];
  };
  androidSdk = androidComposition.androidsdk;
  platformTools = androidComposition.platform-tools;
  firstSdk = pkgs.lib.foldl' pkgs.lib.min 100 androidComposition.platformVersions;
  latestSdk = pkgs.lib.foldl' pkgs.lib.max 0 androidComposition.platformVersions;
}
