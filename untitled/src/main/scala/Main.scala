import scala.util.{Failure, Success}

object Task1 extends App {
	// Дан массив чисел и целевое значение.
	// Напишите программу, которая будет возвращать индексы 2 чисел, сумма которых равна целевому значению.
	def solution(nums: Array[Int], target: Int): Array[Int] = {
		// Your code
		@annotation.tailrec
		def helper(nums: List[Int], target: Int, map: Map[Int, Int], currentIdx: Int): List[Int] = nums match {
			case Nil => Nil
			case (x :: xs) => map.get(target - x) match {
				case Some(idx) => List(idx, currentIdx)
				case None => helper(xs, target, map + (x -> currentIdx), currentIdx + 1)
			}
		}

		helper(nums.toList, target, Map[Int,Int](), 0).toArray
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
	def reverse(lft: Int, rgt: Int): Int = (lft, rgt) match {
		case (0, _) => rgt
		case _ => reverse(lft / 10, rgt * 10 + lft % 10)
	}

	// Определите, является ли целое число палиндромом.
	def solution(x: Int): Boolean = {
		// Your code
		if (x < 0) {
			false
		} else {
			x == reverse(x, 0)
		}
	}

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
	def solution(s: String): Int = {
		val (result, _) = s.toList.foldLeft((0, 0))((pair, char) => {
			val (max, current) = pair
			char match {
				case ' ' => (Math.max(current, max), 0)
				case _ => (max, current + 1)
			}
		})

		result
	}

	println(s"Task 3 = ${solution("Hello World")}")
	// Task 3 = 5
}


object Task4 extends App {
	def solution(nums: Array[Int], target: Int): Int = {
		// Your code
		@annotation.tailrec
		def findInversionIdx(nums: Array[Int], leftIdx: Int, rightIdx: Int): Int = {
			if (rightIdx - leftIdx <= 1) {
				return leftIdx
			}

			val pivotIdx = (rightIdx + leftIdx) / 2
			val pivot = nums(pivotIdx)
			val right = nums(rightIdx)

			if (pivot > right) {
				findInversionIdx(nums, pivotIdx, rightIdx)
			} else {
				findInversionIdx(nums, leftIdx, pivotIdx)
			}
		}

		@annotation.tailrec
		def binSearch(nums: Array[Int], target: Int, leftIdx: Int, rightIdx: Int): Int = {
			val pivotIdx = (rightIdx + leftIdx) / 2
			val pivot = nums(pivotIdx)

			if (leftIdx > rightIdx) {
				-1
			} else {
				pivot.compareTo(target) match {
					case x if (x < 0) => binSearch(nums, target, pivotIdx + 1, rightIdx)
					case x if (x > 0) => binSearch(nums, target, leftIdx, pivotIdx - 1)
					case x if (x == 0) => pivotIdx
				}
			}

		}
		// индекс, после которого идет вторая отсортированная часть
		val inversionIdx = findInversionIdx(nums, 0, nums.length - 1)

		if (target < nums(0)) {
			binSearch(nums, target, inversionIdx + 1, nums.length - 1)
		} else {
			binSearch(nums, target, 0, inversionIdx)
		}
	}

	println(s"Task 4 = ${solution(Array(4, 5, 6, 7, 0, 1, 2), 0)}")
	// Task 4 = 4

	println(s"Task 4 = ${solution(Array(4, 5, 6, 7, 0, 1, 2), 3)}")
	// Task 4 = -1

	println(s"Task 4 = ${solution(Array(1), 0)}")
	// Task 4 = -1
}


object Task5 extends App {
	// Дано подмножество различных целых чисел. Верните все возможные подмножества (мощность множества).
	@annotation.tailrec
	def getNthSubset(nums: Array[Int], idx: Int, depth: Int, subset: List[Int]): List[Int] = {
		if (idx == 0) {
			subset
		} else {
			val nextSubset = if (idx % 2 == 1) {
				subset :+ nums(depth)
			} else {
				subset
			}

			getNthSubset(nums, idx / 2, depth + 1, nextSubset)
		}
	}

	def solution(nums: Array[Int]): List[List[Int]] = {
		// Your code
		val seq = for (i <- Range(0, 1 << nums.length))
			yield getNthSubset(nums, i, 0, List())

		seq.toList
	}

	println(s"Task 5 = ${solution(Array(1, 2, 3))}")
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
	// Даны два не пустых связных списка, представляющих два неотрицательных целых числа.
	// Цифры хранятся в обратном порядке, и каждый из их узлов содержит одну цифру.
	// Суммируйте два числа и верните их в виде связанного списка.
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
	// Дан массив неотрицательных целых чисел, где вы изначально располагаетесь на начальном индексе массива.
	// Каждый элемент массива представляет вашу максимальную длину прыжка в этой позиции.
	// Определите, сможете ли вы достичь последнего индекса.
	def dfs(nums: Array[Int], currentIdx: Int): Boolean = {
		if (currentIdx >= nums.length) {
			false
		} else {
			if (currentIdx == nums.length - 1) {
				true
			} else {
				val maxJump = nums(currentIdx)
				Range(0, maxJump)
				  .map(currentIdx + _ + 1)
				  .exists(idx => dfs(nums, idx))
			}
		}
	}

	def solution(nums: Array[Int]): Boolean = dfs(nums, 0)

	println(s"Task 7 = ${solution(Array(2, 3, 1, 1, 4))}")
	// Task 7 = true

	println(s"Task 7 = ${solution(Array(3, 2, 1, 0, 4))}")
	// Task 7 = false

	println(s"Task 7 = ${solution(Array(3, 2, 3, 0, 0, 0))}")
	// Task 7 = true
}

object Task8 extends App {
	// По заданной строке найдите длину самой длинной подстроки без повторяющихся символов.

	def maxUniqueCharsChainLength(list: List[Char], currentLength: Int, maxLength: Int, metChars: Set[Char]): Int = list match {
		case Nil => maxLength
		case (x :: xs) => {
			val (current, max, chars) = if (metChars.contains(x)) {
				(1, Math.max(maxLength, currentLength), Set(x))
			} else {
				(currentLength + 1, Math.max(maxLength, currentLength + 1), metChars + x)
			}

			maxUniqueCharsChainLength(xs, current, max, chars)
		}
	}

	def solution(s: String): Int = maxUniqueCharsChainLength(s.toList, 0, 0, Set())

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
			case (Nil, Nil) => true

			case (x :: xs, y :: '*' :: ys) => {
				if (x == y || y == '.') {
					checkStr(xs, template)
				} else {
					checkStr(x :: xs, ys)
				}
			}

			case (x :: xs, y :: ys) => {
				if (x == y || y == '.') {
					checkStr(xs, ys)
				} else {
					false
				}
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
	def rotateOnce(matrix: Array[Array[Int]], dim: Int, offset: Int): Unit = {
		val minIdx = offset
		val maxIdx = offset + dim - 1

		val lastTop = matrix(minIdx)(maxIdx)
		val lastRight = matrix(maxIdx)(maxIdx)
		val lastBottom = matrix(maxIdx)(minIdx)

		// rotate top
		for (i <- Range(0, dim - 1)) {
			matrix(minIdx)(maxIdx - i) = matrix(minIdx)(maxIdx - i - 1)
		}

		// rotate right
		for (i <- Range(0, dim - 1)) {
			matrix(maxIdx - i)(maxIdx) = matrix(maxIdx - i - 1)(maxIdx)
		}

		// rotate bottom
		for (i <- Range(0, dim - 1)) {
			matrix(maxIdx)(minIdx + i) = matrix(maxIdx)(minIdx + i + 1)
		}

		// rotate left
		for (i <- minIdx to maxIdx - 1) {
			matrix(minIdx + i)(minIdx) = matrix(minIdx + i + 1)(minIdx)
		}

		matrix(minIdx + 1)(maxIdx) = lastTop
		matrix(maxIdx)(maxIdx - 1) = lastRight
		matrix(maxIdx - 1)(minIdx) = lastBottom
	}

	def rotate(matrix: Array[Array[Int]], dim: Int, offset: Int): Unit = {
		if (dim <= 1) {

		} else {
			for (_ <- 0 to dim - 2) {
				rotateOnce(matrix, dim, offset)
			}
			rotate(matrix, dim - 2, offset + 1)
		}
	}


	def solution(matrix: Array[Array[Int]]): Array[Array[Int]] = {
		rotate(matrix, matrix.length, 0)
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

	def genPerm(nums: List[Int], state: List[Int]): List[List[Int]] = nums match {
		case Nil => List(state)
		case nums => {
			rotations(nums).flatMap(r => {
				val (x :: xs) = r
				genPerm(xs, state :+ x)
			})
		}
	}

	def rotations(nums: List[Int]): List[List[Int]] = {
		val rot = for (i <- 0 to nums.length - 1)
			yield (rotateRight(nums, i))

		rot.toList
	}

	def rotateRight(nums: List[Int], n: Int): List[Int] = {
		nums.takeRight(n) ++ nums.dropRight(n)
	}

	def solution(nums: Array[Int]): List[List[Int]] = {
		val list = nums.toList
		genPerm(list, List())
	}

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
		val r = ".*".r
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

	def validateByte(s: String): Try[Boolean] = {
		val reg = "0|([1-9]{1,1}\\d{0,2})".r
		if (reg.matches(s)) {
			for (num <- Try(s.toInt))
				yield num <= 255
		} else {
			Success(false)
		}
	}

	def helper(s: String, parts: List[String]): List[List[String]] = {
		if (s.isEmpty) {
			List(parts)
		} else {
			if (parts.length > 4) {
				Nil
			} else {
				(1 to 3).flatMap(i => {
					val (head, tail) = s.splitAt(i)
					helper(tail, parts :+ head)
				}).toList
			}
		}
	}

 	def solution(s: String): List[String] = {
		println(s)
		val ips = helper(s, List()).filter(split => {
			split.length == 4 && split.forall(part => validateByte(part) match {
				case Success(v) => v
				case Failure(_) => false
			})
		}).map(_.mkString("."))

		Set.from(ips).toList
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

	//println(s"Task 13 = ${solution("2552551113500001111010010101023")}")
	// Task 13 = List()
}

object Task14 extends App {

	def helper(str: String, wordDict: List[String], buf: List[String], result: List[String]): List[String] = {
		if (str.isEmpty) {
			buf.mkString(" ") :: result
		} else {
			val prefixes = for {
				w <- wordDict
				if (str.startsWith(w))
			} yield (w)

			if (prefixes.isEmpty) {
				Nil
			} else {
				prefixes.flatMap(p => helper(str.drop(p.length), wordDict, buf :+ p, result))
			}
		}

	}

	def solution(s: String, wordDict: List[String]): List[String] = {
		helper(s, wordDict, List(), List())
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
	def landDfs(grid: Array[Array[Char]], pos: (Int, Int), visited: Set[(Int, Int)]): Set[(Int,Int)] =  {
		val (x, y) = pos
		val H = grid.length
		val L = grid(0).length //?
		if (
		  x >= H
		  || y >= L
		  || visited.contains((x, y))
		  || grid(x)(y) != '1') {
			visited
		} else {
			landDfs(grid, (x + 1, y), visited + ((x, y))) ++ landDfs(grid, (x, y + 1), visited + ((x, y)))
		}
	}

	def solution(grid: Array[Array[Char]]): Int = {
		// Your code
		landDfs(grid, (0, 0), Set())
		val N = grid.length

		val indicies = (0 to N).flatMap(i => (0 to N).map(j => (i, j)))

		val (landsCnt, _) = indicies.foldLeft((0, Set[(Int, Int)]()))(
			(state, pos) => {
				val (cnt, visited) = state
				val visitedFromPos = landDfs(grid, pos, visited)
				if (visitedFromPos.size > visited.size) {
					(cnt + 1, visited ++ visitedFromPos)
				} else {
					(cnt, visited)
				}
			}
		)

		landsCnt
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