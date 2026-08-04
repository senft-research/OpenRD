# OpenRD
***
OpenRD is an Open-Source Project for the purposes of replicating the communication protocol utilised by the Rudia 
Controllers utilised by their line of Laser cutters. The library strives to not only allow a near identical ability to
communicate with the controllers, but also fully document the protocol as fully as possible. 

**Disclaimer:** _This software is not endorsed by Rudia, and should be utilised at the user's own risks_

***

## Why was this made?
### Academic Use-Cases 
Whilst the Rudia software is satisfactory for working with their laser cutters, it is seldom enough for academic purposes. 
Student projects that require custom algorithms, dissertations into creating custom UIs etc. Having the ability to 
communicate with Rudia Laser Cutters directly makes these use-cases more feasible.

### Documentation of Commands
Furthermore, the current APIs for Rudia Controllers are not well documented, typically being embedded within other projects
such as Visicut. As such, there is no real need for a full explanation of every aspect of the command protocol. This has 
left quite a significant gap in the understanding of the Communication Protocols, which this project endeavours to fill.

### Encapsulation of Commands
In most the APIs for Rudia Controllers, the commands are hardcoded where needed, with only the encoding method being
encapsulated. As such, it proves difficult to fully understand what is being done where without independent research. As
such, this library intends to encapsulate the commands themselves via the `RDCommandHex` enums, with each enum representing
the hex of a Rudia command. Furthermore, each command (and its arguments) are encapsulated as `RDCommand` and
`RDCommandArg` instances respectfully. This leads to much less confusion as to what is required for each command utilised
throughout the library's logic. 

## Usage and API
As of the moment, the API is quite simple. A set of `List<List<RDPoint>>` is utilised to represent the paths that belong
to each layer of the job. With each layer represented by an `RDLayer` instance that contains:

- The speed (in mm/s) as an integer
- The power levels to be utilised (as an `RDPoint` representing the min and max percentage)
- The `RDColour` that visually represents the layer (i.e. the colour to represent the layer as in appropriate software)
- The "frequency" of the laser. This is something that needs to be researched more in the official Rudia software.
- The boundary box of the layer. However, it is to remain `null` for the time being, as it is set later. The original
logic of this parameter is because the `RDLayer` is currently a record, for convenience. However, this is quite limiting
and will likely change in a future PR.

Below is a simple example of how to write a single layer to a `.rd` file named `test.rd`.

```java
        List<RDPoint> markPath1 = List.of(new RDPoint(12, 10), new RDPoint(38, 25), new RDPoint(12, 40), new RDPoint(12, 10));
        List<RDPoint> markPath2 = List.of(new RDPoint(16, 6), new RDPoint(10, 6), new RDPoint(13, 3), new RDPoint(16, 6));
        List<RDPoint> markPath3 = List.of(new RDPoint(60, 6), new RDPoint(54, 6), new RDPoint(57, 3), new RDPoint(60, 6));

        List<List<RDPoint>> pathsListMark = new ArrayList<>();
        pathsListMark.add(markPath1);
        pathsListMark.add(markPath2);
        pathsListMark.add(markPath3);
        RDLayer layer0 = new RDLayer(pathsListMark, 100, new RDPoint(10, 18), new RDColour(0, 255, 0), 20.0f, null);
        OpenRD.getInstance().set(layer0, 0);
        OpenRD.getInstance().write(true, "test.rd");
```
As elaborated on in the acknowledgements, the properties for the example layer are taken from the Python-based Rudia Open
Source project by jweiger.

## Contributing 
The project is more than happy to accept contributions! The main areas of focus at the moment are: 
- Writing the Javadocs each of the `RDCommandHex` enums, quite a few of the command's specific purposes are not fully
defined in existing documentation. 
- Writing the full documentation of the protocol, explaining the various aspects of the .rd file (header, body, trailer).
- The current API is quite simple, and does not account for multiple `.rd` files being created in the same session. A more
versatile set of methods for the API would be ideal for utilising it for other applications. Please refer to the CONTRIBUTING.md
for more information.

## Acknowledgements
I would like to acknowledge [jnweiger](https://github.com/jnweiger) and his work on the
[Rudia Python Project](https://github.com/jnweiger/ruida-laser) for being the primary inspiration for this library. Without
the groundwork he laid down, nearly a decade ago, this project would not be possible.

Furthermore, I would like to acknowledge [Joe Pfeiffer](https://github.com/JoePfeiffer), who has been subject to my mild
ramblings whilst getting my footing on this project. 





