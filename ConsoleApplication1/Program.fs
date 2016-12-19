open SudukoSolver
open GridPrinter

[<EntryPoint>]
let main argv =
    let start = System.DateTime.Now
    let solution = finishedGrids.Force()
    let (finishedGrids, allGrids) = solution
    printSolution (finishedGrids)
    let indexedGrids = indexGridsByHash allGrids
    traverseSolution finishedGrids.[0] indexedGrids
    let endTime = System.DateTime.Now
    let span = endTime.Subtract(start).ToString()
    printf "%s" span
    System.Console.Read() |> ignore
    0 // return an integer exit code