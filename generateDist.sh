#!/bin/bash

rm -rf dist

mkdir -p dist/
cp README.md dist/

#Copy sounds
mkdir -p dist/sounds
cp sounds/* dist/sounds

#Copy shaders
mkdir -p dist/shaders
cp shaders/*.v.glsl dist/shaders
cp shaders/*.g.glsl dist/shaders
cp shaders/*.f.glsl dist/shaders

# Copy libs
mkdir -p dist/lib/lwjgl/native/linux
mkdir -p dist/lib/lwjgl/native/windows
cp lib/lwjgl/native/linux/*.so dist/lib/lwjgl/native/linux
cp lib/lwjgl/native/windows/*.dll dist/lib/lwjgl/native/windows

# Copy graphics
mkdir -p dist/graphics/output
mkdir -p dist/graphics/icons
cp graphics/output/*.v3draw dist/graphics/output
cp graphics/icons/*.png dist/graphics/icons

# Copy fonts
mkdir -p dist/fonts
cp fonts/*.ttf dist/fonts

# Copy firmware
mkdir -p dist/drivers
cp drivers/*.js dist/drivers

# Copy assets
cp -r assets dist/assets

# Copy res
cp -r res dist/res
