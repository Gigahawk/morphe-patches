{ inputs, ... }:
{
  perSystem =
    {
      self',
      pkgs,
      ...
    }:
    {
      packages = {
        morphe-desktop = pkgs.callPackage ./_pkgs/morphe-desktop/package.nix { };

        jadx = pkgs.callPackage ./_pkgs/jadx-with-plugins/package.nix { };
      };
    };
}
