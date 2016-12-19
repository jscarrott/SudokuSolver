module GridParser

open SudokuGrid

let ReadInPuzzle = System.IO.File.ReadAllLines(".\Puzzle.txt")

let parseLines =
    ReadInPuzzle
    |> Array.map (fun x -> x.Split ',')
    |> Array.toList

let parseCells =
    [ for line in parseLines do
            yield Array.toList line ]

let parseCellValue value =
    if (int value > 0) then [ int value ]
    else [ 1..9 ]

let startingGrid =
    {numberOfRows = parseCells.Length; numberOfColumns = parseCells.[0].Length;
           cells = [ for i = 0 to parseCells.Length - 1 do
                        for j = 0 to parseCells.[0].Length - 1 do
                           yield {row = i + 1; column = j + 1; possibleValues = parseCellValue parseCells.[i].[j]} ]; level = 1}
//            printfn "this cell"
//            printCell cell
//            printfn "Compared to"
//            printCell cells.Head