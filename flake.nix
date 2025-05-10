{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
  };

  outputs =
    { nixpkgs, ... }:
    let
      forAllSystems = f: with nixpkgs; lib.genAttrs lib.systems.flakeExposed (s: f legacyPackages.${s});
    in
    {
      devShells = forAllSystems (
        pkgs:
        let
          jdk = pkgs.jdk;
        in
        {
          default = pkgs.mkShellNoCC {
            packages = with pkgs; [
              jdk
              (gradle.override {
                java = jdk;
              })
            ];
            JAVA_HOME = jdk.home;
            LD_LIBRARY_PATH = pkgs.lib.makeLibraryPath (
              with pkgs;
              [
                glfw
                libpulseaudio
                openal
                libGL
              ]
            );
          };
        }
      );
    };
}
