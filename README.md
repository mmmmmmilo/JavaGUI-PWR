# Academic Programming Projects Repository

A collection of university-level Java applications developed for the **Programming Languages** course. Each sub-project explores core concepts of the Java programming language, ranging from graphical user interfaces (Swing/AWT) to graphics processing, and multi-threaded, real-time concurrent simulation systems.

---

## Repository Overview

This repository is designed to host multiple distinct, self-contained educational projects. It demonstrates software engineering paradigms such as object-oriented design, dynamic event-driven graphical interfaces, robust user input parsing/verification, and safe multi-threaded concurrency in Java.

### Structure
The repository is divided into the following directories:
* **`L4/`**: Function Chart Generator – Interactive application to plot custom mathematical functions (linear, quadratic, sinusoidal) with real-time UI validation.
* **`L5/`**: Image Cropping Tool – Desktop image processor for loading, selecting, and cropping PNG images with precise coordinate transformation.
* **`L6/`**: Traffic Intersection Simulation – High-concurrency multithreaded simulation featuring autonomous car threads, traffic light control, and active collision avoidance.

---

## 1. Function Chart Generator (`L4`)

### Description
The **Function Chart Generator** is an interactive Swing desktop application that allows users to plot, manage, and inspect mathematical curves. The program renders linear, quadratic, and sinusoidal functions dynamically, adjusting the grid scale based on the maximum and minimum values of all active curves.

A core highlight is the robust real-time user-input verification: the application continuously parses domain inputs (`xmin`, `xmax`, sample size `k`) and highlights fields in red to prevent division-by-zero, empty graphs, or invalid bounds, keeping the interface bulletproof and crash-resistant.

### Key Features
* **Multi-Function Support**: Linear ($y = ax + b$), Quadratic ($y = ax^2 + bx + c$), and Trigonometric Sine ($y = a \sin(x - b\pi) + c$) equations.
* **Dynamic Chart Rendering**: Automatically computes coordinate transformations, drawing X/Y axes and plotting smooth multi-colored curves.
* **CRUD Management**: Easily Add, Edit, or Delete functions through an interactive side panel list and configuration dialogs.
* **Real-Time Input Validation**: Instantly flags invalid inputs (e.g., $x_{min} \ge 0$, $x_{max} \le 0$, $k < 2$, or non-numeric values) using dynamic background colors and disables plotting until corrected.

### Technologies Used
* **Language**: Java SE (JDK 17+)
* **GUI Framework**: Swing (components, layout managers, interactive models, dialog windows)
* **Graphics API**: AWT (`java.awt.Graphics` Custom Drawing)

### Compilation & Run Instructions
Navigate to the repository root directory and run:

```bash
# Compile all source files into a bin directory
javac L4/src/*.java -d L4/bin

# Run the application
java -cp L4/bin Charts
```

### Showcase / Gallery
<div align="center">
  <img src="assets/l4_main.png" width="48%" />
  <img src="assets/l4_dialog.png" width="48%" />
</div>

---

## 2. Image Cropping Tool (`L5`)

### Description
The **Image Cropping Tool** is a desktop utility tailored for lossless, pixel-perfect PNG cropping. Users can load an image, specify a cropping region using two distinct visual methods, and export the selection. 

The tool implements custom calculations to bridge the gap between user mouse interactions on the scaled UI viewport and the actual coordinates of the original, high-resolution source image, preserving exact visual boundaries.

### Key Features
* **Two Selection Techniques**:
  * *Rectangle Selection*: Drag-and-drop bounding box selection (rendered in green).
  * *Lines Selection*: Moveable horizontal and vertical guidelines (rendered in blue dashed lines) for precise cropping boundaries.
* **Pixel and Color Inspector**: Displays current mouse coordinates and the hexadecimal RGB value (`#RRGGBB`) of the pixel under the cursor in real-time.
* **Lossless Cropping**: Automatically transforms GUI-scaled dimensions back to original file resolution, extracting exact subimages.
* **Shortcut Bindings**: Complete keyboard-driven workflow for maximum productivity:
  * `W` - Load PNG Image
  * `Z` - Save Cropped Image
  * `K` - Switch to Rectangle Mode
  * `L` - Switch to Lines Mode
  * `C` - Clear Selection & Reset Mode
  * `Q` - Quit Application
* **Contextual Access**: Identical actions are accessible via a right-click pop-up menu.

### Technologies Used
* **Language**: Java SE (JDK 17+)
* **GUI**: Java Swing & AWT (`JFileChooser`, `JPopupMenu`, Event Listeners)
* **Image Processing**: standard `javax.imageio.ImageIO`, `java.awt.image.BufferedImage`, `java.awt.geom`

### Compilation & Run Instructions
Navigate to the repository root directory and run:

```bash
# Compile all source files into a bin directory
javac L5/src/*.java -d L5/bin

# Run the application
java -cp L5/bin ImageCroppingTool
```

### Showcase / Gallery
<div align="center">
  <img src="assets/l5_main.png" width="48%" />
  <img src="assets/l5_selection.png" width="48%" />
</div>

---

## 3. Traffic Intersection Simulation (`L6`)

### Description
The **Traffic Intersection Simulation** is a highly concurrent graphical simulator modeling vehicle movement through a busy 4-way intersection. Built around the producer-consumer and autonomous agent paradigms, every single car on the screen runs in its own dedicated execution thread. 

The cars dynamically adapt their velocities, handle lanes, execute turns, and perform active collision avoidance based on leading vehicles and multi-phase traffic lights. Safe memory sharing is implemented using modern concurrent collections.

### Key Features
* **Multi-Threaded Agent Model**: Every spawned vehicle is an isolated thread managing its own lifecycle, physics calculations, and movement loops.
* **Automated Traffic Light Controller**: A central controller thread manages horizontal and vertical light configurations, switching states every 5 seconds.
* **Real-time Collision Avoidance**: Vehicles detect preceding cars in their lane and direction, matching their speed or coming to a complete stop to maintain a safe distance.
* **Complex Path Navigation**: Cars randomly choose whether to go straight or activate turn signals, smoothly navigating lanes and changing directions at coordinates.
* **Thread-Safe State Management**: Employs `CopyOnWriteArrayList` to handle real-time modifications (car spawns, out-of-bounds removals) without thread-safety issues.
* **Smooth Rendering Loop**: A dedicated interface update thread redraws the environment at ~60 FPS.

### Technologies Used
* **Language**: Java SE (JDK 17+)
* **Concurrency Primitives**: Java Threads, `Runnable`, Swing `Timer`, `CopyOnWriteArrayList`, Thread synchronization/sleeping.
* **GUI / Render engine**: Swing, AWT standard custom shapes (`paintComponent`).

### Compilation & Run Instructions
Navigate to the repository root directory and run:

```bash
# Compile all source files into a bin directory
javac L6/src/*.java -d L6/bin

# Run the application
java -cp L6/bin TrafficIntersection
```

### Showcase / Gallery
<div align="center">
  <img src="assets/l6_overview.png" width="48%" />
  <img src="assets/l6_night_traffic.png" width="48%" />
</div>
