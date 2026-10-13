{ inputs, ... }:
{
  perSystem =
    {
      self',
      pkgs,
      ...
    }:
    let
      sdk = import ./_android-sdk.nix { inherit pkgs; };
    in
    {
      devShells = {
        default = pkgs.mkShell rec {
          packages = with pkgs; [
            jdk21
            gradle
            maven
            sdk.androidSdk
            sdk.platformTools
          ];

          ANDROID_SDK_ROOT = "${sdk.androidSdk}/libexec/android-sdk";
          ANDROID_HOME = ANDROID_SDK_ROOT;
          ANDROID_NDK_ROOT = "${ANDROID_SDK_ROOT}/ndk-bundle";

          # mkShell's setup hook already exports JAVA_HOME for jdk packages in
          # nixpkgs, but being explicit is harmless:
          #JAVA_HOME = "${pkgs.jdk21}/lib/openjdk";
        };
      };
    };
}
