module SudokuGrid

open SudokuCell

type Grid(numberOfRows : int, numberOfColumns : int, cells : List<Cell>, level : int) as self =
    member this.solvedCells   = self.Cells |> List.filter (fun (x : Cell) -> Solved x)
    member this.unsolvedCells = self.Cells |> List.filter (fun (x : Cell) -> not (Solved x))
    member this.isFinished    = this.unsolvedCells.Length = 0
    member this.Level         = level
    member this.Rows          = [ 1..numberOfRows ]
    member this.Columns       = [ 1..numberOfColumns ]
    member this.Cells =
        if (cells = List.Empty) then
            Seq.toList (seq {
                            for i in this.Rows do
                                for j in this.Columns do
                                    yield { row = i
                                            column = j
                                            possibleValues = [ 1..9 ] } //How cool is this type inference!
                        })
        else cells

