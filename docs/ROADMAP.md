r# Project Objectives

## Project Objectives

To build a 3D engine where movement and interactions in X and Y follow hyperbolic geometry, while Z remains Euclidean, and provide well-documented research to explain the theory and implementation.  

## Roadmap

### Understanding the Poincaré Disk (2D)  
- [x] Study hyperbolic geometry and the Poincaré disk model  
- [x] create basic visualization of the Poincaré disk in a hight dimension
- [ ] Implement geodesics (shortest paths in hyperbolic space)
- [ ] Implement segments of geodesics and circle
- [ ] Write a docs explaining the basics of the Poincaré disk and its applications

## 2D Hyperbolic Engine  
- [ ] Create a rendering engine for the Poincaré disk  
- [ ] Implement player movement in hyperbolic space  
- [ ] Add smooth rotation based on hyperbolic transformations  
- [ ] Handle object scaling and distance perception  
- [ ] Experiment with basic interactions and physics in the 2D hyperbolic world  
- [ ] Extend the docs to cover the implementation details of the 2D engine  

## Transition to 3D (Hybrid Euclidean/Non-Euclidean Space)  
- [ ] Define a 3D space where X and Y are hyperbolic, but Z remains Euclidean  
- [ ] Extend the 2D hyperbolic transformations to 3D (Poincaré disk → Poincaré ball projection)  
- [ ] Implement a camera system that correctly handles depth in this hybrid space  
- [ ] Experiment with rendering techniques for objects in non-Euclidean space  
- [ ] Add a new section to the docs discussing the challenges of extending to 3D  

## Advanced 3D Rendering & Interactions  
- [ ] Develop smooth player movement in the hybrid 3D space  
- [ ] Implement lighting and shading adapted to hyperbolic geometry  
- [ ] Handle interactions between objects using non-Euclidean distance metrics
- [ ] Document the rendering techniques in the docs, with illustrations