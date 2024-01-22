import scala.util.{Failure, Success}

object Util {
	def rotateRight[A](l: List[A], n: Int) = l.takeRight(n) ++ l.dropRight(n)
	def rotations[A](l: List[A]) = Range(0, l.length).map(rotateRight(l, _))
}

object Task1 extends App {
	def solution(nums: Array[Int], target: Int): Array[Int] = {
		// Your code
		@annotation.tailrec
		def findIndexesMatchingTarget(idx: Int, valueToIndex: Map[Int, Int]): Array[Int] = {
			if (idx >= nums.length) {
				Array.empty
			} else {
				val value = nums(idx)
				valueToIndex.get(target - value) match {
					case Some(matchIdx) => Array(matchIdx, idx)
					case None => findIndexesMatchingTarget(idx + 1, valueToIndex + (value -> idx))
				}
			}
		}

		findIndexesMatchingTarget(0, Map.empty)
	}

	println(s"Task 1 = ${solution(Array(2, 7, 11, 15), 9).toList}")
	// Task 1 = List(0, 1)

	println(s"Task 1 = ${solution(Array(3, 2, 4), 6).toList}")
	// Task 1 = List(1, 2)

	println(s"Task 1 = ${solution(Array(3, 3), 6).toList}")
	// Task 1 = List(0, 1)
}

object Task2 extends App {
	@annotation.tailrec
	def reverse(lft: Int, rgt: Int): Int = if (lft == 0) rgt else reverse(lft / 10, rgt * 10 + lft % 10)

	def solution(x: Int): Boolean = if (x < 0) false else x == reverse(x, 0)

	println(s"Task 2 = ${solution(121)}")
	// Task 2 = true

	println(s"Task 2 = ${solution(-121)}")
	// Task 2 = false

	println(s"Task 2 = ${solution(10)}")
	// Task 2 = false

	println(s"Task 2 = ${solution(-101)}")
	// Task 2 = false
}

object Task3 extends App {
	// Your code
	def solution(s: String): Int = s.split(' ').lastOption match {
		case Some(word) => word.length
		case _ => 0
	}

	println(s"Task 3 = ${solution("Hello World")}")
	// Task 3 = 5
}

object Task4 extends App {
	def solution(nums: Array[Int], target: Int): Int = {
		// Your code
		@annotation.tailrec
		def search(nums: Array[Int], lftIdx: Int, rgtIdx: Int): Int = {
			if (lftIdx > rgtIdx) {
				-1
			} else {
				val pivotIdx = (lftIdx + rgtIdx) / 2
				val pivot = nums(pivotIdx)

				if (target > pivot) {
					val right = nums(rgtIdx)
					if (right < target) {
						search(nums, lftIdx, pivotIdx - 1)
					} else {
						search(nums, pivotIdx + 1, rgtIdx)
					}
				} else if (target < pivot) {
					val lft = nums(lftIdx)
					if (lft > target) {
						search(nums, pivotIdx + 1, rgtIdx)
					} else {
						search(nums, lft, pivotIdx - 1)
					}
				} else {
					pivotIdx
				}
			}
		}

		search(nums, 0, nums.length - 1)
	}

	println(s"Task 4 = ${solution(Array(4, 5, 6, 7, 0, 1, 2), 0)}")
	// Task 4 = 4

	println(s"Task 4 = ${solution(Array(4, 5, 6, 7, 0, 1, 2), 3)}")
	// Task 4 = -1

	println(s"Task 4 = ${solution(Array(1), 0)}")
	// Task 4 = -1
}

object Task5 extends App {
	def solution(nums: Array[Int]): List[List[Int]] = {
		val N = nums.length
		List.range(0, N + 1).flatMap(nums.combinations(_).map(_.toList))
	}

	println(s"Task 5 = ${solution(Array(1, 2, 3, 4))}")
	// Task 5 = List(
	//   List(3),
	//   List(1),
	//   List(2),
	//   List(1, 2, 3),
	//   List(1, 3),
	//   List(2, 3),
	//   List(1, 2),
	//   List()
	// )
}

object Task6 extends App {
	case class ListNode(x: Int = 0, next: Option[ListNode] = None)

	def merge(l1: ListNode, l2: ListNode, carry: Int): ListNode = (l1, l2) match {
		case (ListNode(l, Some(nextL)), ListNode(r, Some(nextR)))
		=> ListNode((l + r + carry) % 10, Some(merge(nextL, nextR, (l + r + carry) / 10)))

		case (ListNode(l, Some(nextL)), ListNode(r, None)) => {
			val nextCarry = (l + r + carry) / 10
			val digit = (l + r + carry) % 10

			ListNode(digit, Some(merge(nextL, ListNode(0, None), nextCarry)))
		}

		case (ListNode(l, None), ListNode(r, Some(nextR))) => {
			val nextCarry = (l + r + carry) / 10
			val digit = (l + r + carry) % 10

			ListNode(digit, Some(merge(ListNode(0, None), nextR, nextCarry)))
		}

		case (ListNode(l, None), ListNode(r, None)) => {
			val nextCarry = (l + r + carry) / 10
			val digit = (l + r + carry) % 10

			if (nextCarry == 0) {
				ListNode(digit, None)
			} else {
				ListNode(digit, Some(ListNode(nextCarry, None)))
			}
		}
	}

	def solution(l1: ListNode, l2: ListNode): ListNode = merge(l1, l2, 0)

	println(s"Task 6 = ${solution(ListNode(2, Some(ListNode(4, Some(ListNode(3))))), ListNode(5, Some(ListNode(6, Some(ListNode(4))))))}")
	// Task 6 = ListNode(7, Some(ListNode(0, Some(ListNode(8, None)))))

	println(s"Task 6 = ${solution(ListNode(), ListNode())}")
	// Task 6 = ListNode(0, None)

	println(s"Task 6 = ${
		solution(
			ListNode(9, Some(ListNode(9, Some(ListNode(9, Some(ListNode(9, Some(ListNode(9, Some(ListNode(9, Some(ListNode(9))))))))))))),
			ListNode(9, Some(ListNode(9, Some(ListNode(9, Some(ListNode(9)))))))
		)
	}")
}

// Task 6 = ListNode(8, Some(ListNode(9, Some(ListNode(9, Some(ListNode(9, Some(ListNode(0, Some(ListNode(0, Some(ListNode(0, Some(ListNode(1,None)))))))))))))))
// Task 6 = ListNode(8, Some(ListNode(9, Some(ListNode(9, Some(ListNode(9, Some(ListNode(0, Some(ListNode(0, Some(ListNode(0, Some(ListNode(1, None)))))))))))))))

object Task7 extends App {
	def solution(nums: Array[Int]): Boolean = {
		val lastJump = nums.dropRight(1).foldLeft(1)((prevMaxJump, maxJump) => {
			if (prevMaxJump == 0) {
				0
			} else {
				Math.max(prevMaxJump - 1, maxJump)
			}
		})

		lastJump > 0
	}

	println(s"Task 7 = ${solution(Array(2, 3, 1, 1, 4))}")
	// Task 7 = true

	println(s"Task 7 = ${solution(Array(3, 2, 1, 0, 4))}")
	// Task 7 = false

	println(s"Task 7 = ${solution(Array(3, 2, 3, 0, 0, 0))}")
	// Task 7 = true
}

object Task8 extends App {

	def solution(s: String): Int = {
		val (_, maxLength) = s.foldLeft(Set.empty[Char], 0)((state, c) => {
			val (metChars, maxLength) = state
			if (metChars.contains(c)) {
				(Set(c), Math.max(maxLength, 1))
			} else {
				(metChars + c, Math.max(maxLength, metChars.size + 1))
			}
		})

		maxLength
	}

	println(s"Task 8 = ${solution("abcabcbb")}")
	// Task 8 = 3

	println(s"Task 8 = ${solution("bbbbb")}")
	// Task 8 = 1

	println(s"Task 8 = ${solution("pwwkew")}")
	// Task 8 = 3

	println(s"Task 8 = ${solution("")}")
	// Task 8 = 0
}

object Task9 extends App {

	@annotation.tailrec
	def checkStr(str: List[Char], template: List[Char]): Boolean = {
		(str, template) match {
			case (x :: xs, y :: '*' :: ys) =>
				if (x == y || y == '.') {
					checkStr(xs, template)
				} else {
					checkStr(x :: xs, ys)
				}

			case (x :: xs, y :: ys) =>
				if (x == y || y == '.') {
					checkStr(xs, ys)
				} else {
					false
				}


			case (Nil, Nil) => true
			case (Nil, _ :: '*' :: Nil) => true
			case _ => false
		}
	}

	def solution(s: String, p: String): Boolean = checkStr(s.toList, p.toList)

	println(s"Task 9 = ${solution("aa", "a")}")
	// Task 9 = false

	println(s"Task 9 = ${solution("aa", "a*")}")
	// Task 9 = true

	println(s"Task 9 = ${solution("ab", ".*")}")
	// Task 9 = true

	println(s"Task 9 = ${solution("aab", "c*a*b")}")
	// Task 9 = true

	println(s"Task 9 = ${solution("mississippi", "mis*is*p*.")}")
	// Task 9 = false
}

object Task10 extends App {

	def solution(matrix: Array[Array[Int]]): Array[Array[Int]] = {
		val N = matrix.length

		for (
			i <- 0 until N;
			j <- 0 until i
		) {
			val t = matrix(i)(j)
			matrix(i)(j) = matrix(j)(i)
			matrix(j)(i) = t
		}

		for (
			i <- 0 until N;
			j <- 0 until N / 2
		) {
			val t = matrix(i)(j)
			matrix(i)(j) = matrix(i)(N - j - 1)
			matrix(i)(N - j - 1) = t
		}

		matrix
	}

	val matrix1 = Array(
		Array(1, 2, 3),
		Array(4, 5, 6),
		Array(7, 8, 9)
	)

	val matrix2 = Array(
		Array(5, 1, 9, 11),
		Array(2, 4, 8, 10),
		Array(13, 3, 6, 7),
		Array(15, 14, 12, 16),
	)

	println(s"Task 10 = \n${solution(matrix1).map(_.mkString(" ")).mkString("\n")}")
	//Task 10 = Array(
	//  Array(7, 4, 1),
	//  Array(8, 5, 2),
	//  Array(9, 6, 3)
	//)

	println(s"Task 10 = \n${solution(matrix2).map(_.mkString(" ")).mkString("\n")}")
	//Task 10 = Array(
	//  Array(15, 13, 2, 5),
	//  Array(14, 3, 4, 1),
	//  Array(12, 6, 8, 9),
	//  Array(16, 7, 10, 11)
	//)
}

object Task11 extends App {
	def solution(nums: Array[Int]): List[List[Int]] = nums.toList.permutations.toList

	println(s"Task 11 = ${solution(Array(1, 2, 3))}")
	// Task 11 = List(
	//   List(1, 2, 3),
	//   List(1, 3, 2),
	//   List(2, 1, 3),
	//   List(2, 3, 1),
	//   List(3, 1, 2),
	//   List(3, 2, 1)
	// )
}


object Task12 extends App {
	/*
	Основная масса прикладных программ для компьютера обеспечивает представление чисел в удобной для восприятия человеком форме, т.е. в десятичной системе счисления.
	На компьютере (в частности в языках программирования высокого уровня) числа в экспоненциальном формате (его ещё называют научным) принято записывать в виде MEp, где:

	M — мантисса,
	E — экспонента (от англ. «exponent»), означающая «·10^» («…умножить на десять в степени…»),
	p — порядок
	 */
	def solution(s: String): Boolean = {
		print(s + " ")
		val r = ("\\s*\\-?" +
		  "(" +
		  	"((0|([1-9]{1,1}\\d*))(\\.\\d*)?)|" +
		  	"((0|([1-9]{1,1}\\d*))?(\\.\\d*))" +
		  ")" +
		  "([eE]\\-?(0|[1-9]\\d*))?" +
		  "\\s*").r
		r.matches(s)
	}

	println(s"Task 12 = ${solution("0")}")         // Task 12 = true
	println(s"Task 12 = ${solution(" 0.1 ")}")     // Task 12 = true
	println(s"Task 12 = ${solution("abc")}")       // Task 12 = false
	println(s"Task 12 = ${solution("1 a")}")       // Task 12 = false
	println(s"Task 12 = ${solution("2e10")}")      // Task 12 = true
	println(s"Task 12 = ${solution(" -90e3   ")}") // Task 12 = true
	println(s"Task 12 = ${solution(" 1e")}")       // Task 12 = false
	println(s"Task 12 = ${solution("e3")}")        // Task 12 = false
	println(s"Task 12 = ${solution(" 6e-1")}")     // Task 12 = true
	println(s"Task 12 = ${solution(" 99e2.5 ")}")  // Task 12 = false
	println(s"Task 12 = ${solution("53.5e93")}")   // Task 12 = true
	println(s"Task 12 = ${solution(" --6 ")}")     // Task 12 = false
	println(s"Task 12 = ${solution("-+3")}")       // Task 12 = false
	println(s"Task 12 = ${solution("95a54e53")}")  // Task 12 = false
	println(s"Task 12 = ${solution(".1")}")        // Task 12 = true
	println(s"Task 12 = ${solution("4.")}")        // Task 12 = true
	println(s"Task 12 = ${solution("-.9")}")       // Task 12 = true
	println(s"Task 12 = ${solution("-90E3")}")     // Task 12 = true
}

object Task13 extends App {
	import scala.util.Try
	def inUnsignedByteRange(v: Int) = v >= 0 && v <= 255

	def isValidByteStr(str: String) = {
		val reg = "0|([1-9]{1,1}\\d{0,2})".r
		if (reg.matches(str)) {
			str.toIntOption.map(inUnsignedByteRange(_)).getOrElse(false)
		} else {
			false
		}
	}

	def combineIpsFromString(s: String, parts: List[String]): List[List[String]] = (s, parts.length) match {
		case (_, ln) if (ln > 4) => Nil
		case ("", 4) => List(parts)
		case _ => {
			(1 to 3).flatMap(i => {
				val (head, tail) = s.splitAt(i)
				if (isValidByteStr(head)) {
					combineIpsFromString(tail, parts :+ head)
				} else {
					Nil
				}
			}).toList
		}
	}

 	def solution(s: String): List[String] = {
		combineIpsFromString(s, List.empty).map(_.mkString(".")).toSet.toList
	}

	println(s"Task 13 = ${solution("25525511135")}")
	// Task 13 = List("255.255.11.135", "255.255.111.35")

	println(s"Task 13 = ${solution("0000")}")
	// Task 13 = List("0.0.0.0")

	println(s"Task 13 = ${solution("1111")}")
	// Task 13 = List("1.1.1.1")

	println(s"Task 13 = ${solution("010010")}")
	// Task 13 = List("0.10.0.10", "0.100.1.0")

	println(s"Task 13 = ${solution("101023")}")
	// Task 13 = List("1.0.10.23", "1.0.102.3", "10.1.0.23", "10.10.2.3", "101.0.2.3")

	println(s"Task 13 = ${solution("2552551113500001111010010101023")}")
	// Task 13 = List()
}

object Task14 extends App {
	def solution(s: String, wordDict: List[String]): List[String] = {

		def combine(str: String, buf: List[String]): List[String] = {
			if (str.isEmpty)
				List(buf.mkString(" "))
			else
				wordDict.flatMap(w => if (str.startsWith(w)) combine(str.drop(w.length), buf :+ w) else Nil)
		}

		combine(s, List.empty)
	}

	println(s"Task 14 = ${solution("catsanddog", List("cat", "cats", "and", "sand", "dog"))}")
	// Task 14 = List(
	//   "cats and dog",
	//   "cat sand dog"
	// )

	println(s"Task 14 = ${solution("pineapplepenapple", List("apple", "pen", "applepen", "pine", "pineapple"))}")
	// Task 14 = List(
	//   "pine apple pen apple",
	//   "pineapple pen apple",
	//   "pine applepen apple"
	// )

	println(s"Task 14 = ${solution("catsandog", List("cats", "dog", "sand", "and", "cat"))}")
	// Task 14 = List()

	println(s"Task 14 = ${solution("abcd", List("a", "abc", "b", "cd"))}")
	// Task 14 = List(a b cd)
}

object Task15 extends App {
	val LAND = '1'
	val MARK = '2'

	def solution(grid: Array[Array[Char]]): Int = {
		// Your code
		val H = grid.length
		val L = grid(0).length //?

		def dfsMarker(pos: (Int, Int)): Unit = {
			val (i, j) = pos
			if (i < H && j < L && grid(i)(j) == LAND) {
				dfsMarker((i + 1, j))
				dfsMarker((i, j + 1))
				grid(i)(j) = MARK
			}
		}

		grid.indices.flatMap(i => {
			grid(i).indices.map(j => {
				if (grid(i)(j) == LAND) {
					dfsMarker((i, j))
					1
				} else {
					0
				}
			})
		}).sum
	}

	val grid1 = Array(
		Array('1','1','1','1','0'),
		Array('1','1','0','1','0'),
		Array('1','1','0','0','0'),
		Array('0','0','0','0','0')
	)

	println(s"Task 15 = ${solution(grid1)}")
	// Task 15 = 1

	val grid2 = Array(
		Array('1','1','0','0','0'),
		Array('1','1','0','0','0'),
		Array('0','0','1','0','0'),
		Array('0','0','0','1','1')
	)

	println(s"Task 15 = ${solution(grid2)}")
	// Task 15 = 3
}