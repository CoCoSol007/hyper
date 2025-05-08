<a id="readme-top"></a>
<div align="center">

[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![License][license-shield]][license-url]

</div>

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <img src="docs/logo.svg" width="400">
  <p align="center">
    <br />
    A Non-Euclidean Rendering Engine.
  </p>
</div>

---

**Hyper** is an experimental rendering engine exploring **non-Euclidean spaces**. It begins with the **Poincaré disk model** (2D) and evolves into a hybrid 3D system where the X and Y axes are hyperbolic, and Z remains Euclidean. Using **ray-casting**, the engine renders an infinite hyperbolic maze—an educational and interactive experience designed to visualize **non-Euclidean geometry**, **hyperbolic tilings**, and **maze algorithms**. Concepts are made accessible through animations, simulations, and hands-on exploration.

## Quickstart

Clone the repository
```bash
git clone https://github.com/cocosol007/hyper.git
cd hyper
```

### 3D

To run the 3D view, run the command 
```bash
./gradlew run
```

https://github.com/user-attachments/assets/5156987d-9099-42a6-9bd4-1a86abcd819e

#### 3D Klein model

To run the 3D Klein model view, run the command 
```bash
./gradlew run --args="klein"
```

https://github.com/user-attachments/assets/b18b1862-54cd-4e70-bc3e-eb668aba2c67

#### 3D Gnomonic model

To run the 3D Gnomonic model view, run the command 
```bash
./gradlew run --args="gnomonic"
```

https://github.com/user-attachments/assets/bc754d07-b1c2-435d-b459-7e679d5f27de

#### Ray-Caster Maze

To run the RayCaster maze, run the command 
```bash
./gradlew runRayCaster
```

https://github.com/user-attachments/assets/cd564d2c-b59d-4107-a9d4-a5095251b5a3

### Poincaré and Beltrami–Klein disk model

To run the 2D Poicaré disk model, run the command 
```bash
./gradlew run2D --args='poincare'
```

To run the 2D Klein model, run the command
```bash
./gradlew run2D --args='Klein'
```

https://github.com/user-attachments/assets/cd1cf6c3-3550-4b40-a5ad-6ffeced9316c

### Gnomonic projection

To run the 2D ./gradlew run2D --args='gnomonic'
 model, run the command
```bash
./gradlew run2D --args='gnomonic'
```

https://github.com/user-attachments/assets/23b0f337-2429-4373-aad7-e0cc986aa4b7

## Documentation

### 0. Project Setup
- A . [How to Run the Project](docs/running-the-project.md)
- B . [How to contribute](.github/CONTRIBUTING.md)
- C . [What for?](docs/what_for.md)

### I. Foundations in Hyperbolic Geometry
- A . [Introduction](docs/I/introduction.md)  
- B . [Poincaré Disk Model](docs/I/basic-mathematics-in-the-poincare-disk-model.md)  
- C . [Isometries](docs/I/isometries.md)

### II. Tessellations
- A . [Introduction to Tessellation](docs/II/introduction_to_tesslation.md)
- B . [Coordinate System](docs/II/coordinates_system.md)
- C . [Implementation](docs/II/implementation.md)

### III. Ray Casting
TODO

### IV. In-Depth Explorations
- A . [Other Models](docs/IV/other_models.md)

## Licence

See all about our license [here](/LICENCE)

![GPL3](https://upload.wikimedia.org/wikipedia/commons/c/cb/GPLv3_Logo_filled.png)

## Contributors

[![Contributeurs](https://contrib.rocks/image?repo=cocosol007/hyper)](https://github.com/cocosol007/hyper/graphs/contributors)

[contributors-shield]: https://img.shields.io/github/contributors/cocosol007/hyper.svg?style=for-the-badge

[contributors-url]: https://github.com/cocosol007/hyper/graphs/contributors

[forks-shield]: https://img.shields.io/github/forks/cocosol007/hyper.svg?style=for-the-badge

[forks-url]: https://github.com/cocosol007/hyper/network/members

[stars-shield]: https://img.shields.io/github/stars/cocosol007/hyper.svg?style=for-the-badge

[stars-url]: https://github.com/cocosol007/hyper/stargazers

[issues-shield]: https://img.shields.io/github/issues/cocosol007/hyper.svg?style=for-the-badge

[issues-url]: https://github.com/cocosol007/hyper/issues

[license-shield]: https://img.shields.io/github/license/cocosol007/hyper.svg?style=for-the-badge

[license-url]: https://github.com/cocosol007/hyper/blob/main/LICENCE
