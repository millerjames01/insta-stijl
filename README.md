## Motivation

> "It is possible that, through horizontal and vertical lines constructed with awareness, but not with calculation, led by high intuition, and brought to harmony and rhythm, these basic forms of beauty, supplemented if necessary by other direct lines or curves, can become a work of art, as strong as it is true.”

> \- Piet Mondrian

This project is a library that is meant to emulate the works of Dutch painter Piet Mondrian. His painting style, known as De Stijl (hence the project name), is often recognized for its exclusive use of primary colors, straight lines, and right angles. As a novice programmer, it seemed that it would be easy to use an image library I was familiar with to generate pngs that would look like Mondrian's pieces, while having fun and working on some functional design skill.

I included this quote because Mondrian is creating a clear distinction between what I'm doing and what he's doing. I'm calculating and placing some lines, while he painted with intuition. As I write more and more complex image generators, it becomes clear that I'm trying to teach the computer to build a little more intuitively and a little more in depth.

## Code Example

If you want to see what this project can do right after installation: 

    $ sbt 
    > run

Wait a second and look inside the `samples` directory of the project. There should be 60 freshly generated composition pieces, which should give you a general feel of the capabilities of this project. 

## Installation

A necessity for running this project is to have [sbt](http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html#installing-sbt) on your computer. From there
    
    $ git clone git@github.com:millerjames01/insta-stijl.git
    $ cd insta-stijl
    $ sbt
    > update
    > compile
    
Everything should be up and running from there.

## API Reference

This is a work-in-progress. Once I present this to my humanities class, I should have a lot more time to document, so coming soon!

## Tests

If you check to see some of the newer commit messages, and look at the comments, you can see I'm a little behind in testing. Sorry!

However, the most fundamental parts of the project are tested fairly well, using the `scalatest` package. To run these tests and any tests (in the `test` package), it should be as simple as

    $ sbt
    > test
