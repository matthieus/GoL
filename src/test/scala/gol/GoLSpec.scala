package test

import org.specs2.specification.Scope
import org.specs2.Specification
import org.specs2.mutable

import gol.Board

object GoLSpec extends Specification { def is =
  
  // todo: 
  // - write this with scalacheck
  // - write this in an acceptance style

  "a living Cell should" ^ {
    "live if 2 neigbours are already alive" ! new fewAlive {
      board.aliveWillLive((0,0)) should beTrue
    } ^
    "live if 3 neigbours are already alive" ! new fewAlive {
      board.aliveWillLive((0,-1)) should beTrue
    } ^
    "die if only 0 neighbour alive" ! new fewAlive {
      board.aliveWillLive((3,-2)) should beFalse
    } ^
    "die if only 1 neighbour alive" ! new fewAlive {
      board.aliveWillLive((0,1)) should beFalse
    } ^
    "die if only 4 neighbours alive" ! new moreAlive {
      board.aliveWillLive((1,0)) should beFalse
    } ^
    "die if only 5 neighbours alive" ! new moreAlive {
      board.aliveWillLive((-1,0)) should beFalse
    }
  } ^
  end^
  "a dead Cell should" ^ {
    "live if 3 neighbours are already alive" ! new fewAlive {
      board.deadWillLive(1,0) should beTrue
    } ^
    "stay dead if 2 neighbours are already alive" ! new fewAlive {
      board.deadWillLive(1,1) should beFalse
    } ^
    "stay dead if 4 neighbours are already alive" ! new fewAlive {
      board.deadWillLive(1,-1) should beFalse
    } ^
    "stay dead if 1 neighbours are already alive" ! new fewAlive {
      board.deadWillLive(3,-1) should beFalse
    } ^
    "stay dead if 5 neighbours are already alive" ! new moreAlive {
      board.deadWillLive(2,0) should beFalse
    }
  } ^
  end^
  "going to the next round should" ^ {
    "transform pattern A into pattern B" ! new pattern1 {
      A.next must_== B
    }
  } ^
  end


  trait fewAlive extends Scope {
    // board (aliveId!=0):
    // O 1 O O O
    // O 2 O O O
    // O 3 O O O
    // O 4 5 O 6
    // O O O O O
    val board = Board(Set(
        (0,1), //1
        (0,0), //2
        (0,-1),//3
        (0,-2),//4
        (1,-2),//5
        (3,-2) //6
        ))
  }  
  
  trait moreAlive extends Scope {
    // board (aliveId!=0):
    // 6 1 O O 9
    // 7 2 5 O 10
    // 8 3 4 O 11
    // O O O O O
    val board = Board(Set(
        (0,1),  //1
        (0,0),  //2
        (0,-1), //3
        (1,-1), //4
        (1,0),  //5
        (-1,1), //6
        (-1,0), //7
        (-1,-1),//8
        (3,1),  //9
        (3,0),  //10
        (3,-1)  //11
        ))
  }
  
  trait pattern1 extends Scope {
    // pattern A:
    // 0 1 O O
    // 0 2 5 O
    // 0 3 4 O    
    val A = Board(Set(
        (0,1), //1
        (0,0), //2
        (0,-1),//3
        (1,-1),//4
        (1,0)  //5
    ))
    // pattern B:
    // 0 1 2 O
    // 5 0 0 O
    // 0 3 4 O    
    val B = Board(Set(
        (0,1), //1
        (1,1), //2
        (0,-1),//3
        (1,-1),//4
        (-1,0) //5
    ))
  }
}

object GoLDisplay extends mutable.Specification {
  
  "bounds of the alive should" ^ {
    "be defined as (most left x, most bottom y, most right x, most top y)" ! new board {
      board.minXminYmaxXmaxY must_== (-1, -2, 2, 0)
    }
    "still be defined for one alive cell" ! new boardOneAlive {
      board.minXminYmaxXmaxY must_== (1,1,1,1)
    }
    "not fail and return the center of the referential" ! new boardEmpty {
      board.minXminYmaxXmaxY must_== (0,0,0,0)
    }
  }
  trait board extends Scope {
    // board:
    // 0 1 0 3
    // 0 0 2 0
    // 4 0 0 0
    val board = Board(Set(
        (0, 0), //1
        (1,-1), //2
        (2,0),  //3
        (-1,-2) //4
        ))
  }
  trait boardOneAlive extends Scope {
    // board:
    // 0 1 0
    val board = Board(Set(
        (1, 1) //1
        ))
  }
  trait boardEmpty extends Scope {
    // board:
    val board = Board(Set())
  }
}