{
  lib,
  symlinkJoin,
  makeWrapper,
  fetchurl,
  runCommand,
  unzip,
  jadx,
  plugins ? { },
}:

let
  defaultPlugins = {
    "jadx-morphe.zip" = fetchurl {
      url = "https://github.com/hoo-dles/jadx-morphe/releases/download/v1.5.10/jadx-morphe-1.5.10.zip";
      hash = "sha256-p8qq4gV5P9hnqhACCAJCgDbwDJKqw/LrIevHyw4F0WM=";
    };
  };
  installPlugins = if plugins == { } then defaultPlugins else plugins;

  prep =
    name: src:
    if lib.hasSuffix ".zip" name then
      let
        n = lib.removeSuffix ".zip" name;
      in
      {
        name = n;
        path = runCommand "jadx-plugin-${n}" { nativeBuildInputs = [ unzip ]; } ''
          mkdir $out
          unzip -q ${src} -d $out
        '';
      }
    else
      {
        inherit name;
        path = src;
      };

  link = lib.concatMapStringsSep "\n" (p: ''ln -sfn ${p.path} "$d/${p.name}"'') (
    lib.mapAttrsToList prep installPlugins
  );
in
symlinkJoin {
  name = "jadx-with-plugins-${jadx.version}";
  paths = [ jadx ];
  nativeBuildInputs = [ makeWrapper ];
  postBuild = ''
    for b in jadx jadx-gui; do
      wrapProgram $out/bin/$b --run '
        d="''${XDG_CONFIG_HOME:-$HOME/.config}/jadx/plugins/dropins"
        mkdir -p "$d"
        # drop stale store symlinks from previous generations/removed plugins
        find "$d" -maxdepth 1 -type l -lname "/nix/store/*" -delete
        ${link}
      '
    done
  '';
  inherit (jadx) meta;
}
