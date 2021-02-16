.data

				#declaring size of array A

A: .word 1, 3, 5, 9, 13
.text

				#first element of A in $t0

main:	la $t0, A

				#t1-t5 are elements of A

	lw $t1, 0($t0)
	lw $t2, 4($t0)
	lw $t3, 8($t0)
	lw $t4, 12($t0)
	lw $t5, 16($t0)

				#$s1 = 0, $s2 = 5

	addi $s1, $s0, 0
	addi $s2, $s0, 5

				#for(int i = 0, i < 5, i++)

	for:	beq $s1, $s2, done
		#jumping to specific index of array
		beq $s1, 0, zero
		beq $s1, 1, one
		beq $s1, 2, two
		beq $s1, 3, three
		beq $s1, 4, four

				#checking if integer is dividable by 2
				#if it is divide it by 2 for each index
				#then store it in array

		zero:	andi $s0, $t1, 1
			bne $s0, 0, elsezero
			sra $t1, $t1, 1
			sw $t1, 0($t0)
			j cont
		one:	andi $s0, $t2, 1
			bne $s0, 0, elseone
			sra $t2, $t2, 1
			sw $t2, 4($t0)
			j cont
		two:	andi $s0, $t3, 1
			bne $s0, 0, elsetwo
			sra $t3, $t3, 1
			sw $t3, 8($t0)
			j cont
		three:	andi $s0, $t4, 1
			bne $s0, 0, elsethree
			sra $t4, $t4, 1
			sw $t4, 12($t0)
			j cont
		four:	andi $s0, $t5, 1
			bne $s0, 0, elsefour
			sra $t5, $t5, 1
			sw $t5, 16($t0)
			j cont

				#multipyling by 3 then adding 1 for each index

		elsezero:	addi $t6, $t1, 0
				add $t1, $t1, $t6
				add $t1, $t1, $t6
				addi $t1, $t1, 1
				sw $t1, 0($t0)
				j cont
		elseone:	addi $t6, $t2, 0
				add $t2, $t2, $t6
				add $t2, $t2, $t6
				addi $t2, $t2, 1
				sw $t2, 4($t0)
				j cont
		elsetwo:	addi $t6, $t3, 0
				add $t3, $t3, $t6
				add $t3, $t3, $t6
				addi $t3, $t3, 1
				sw $t3, 8($t0)
				j cont
		elsethree:	addi $t6, $t4, 0
				add $t4 $t4, $t6
				add $t4, $t4, $t6
				addi $t4, $t4, 1
				sw $t4, 12($t0)
				j cont
		elsefour:	addi $t6, $t5, 0
				add $t5 $t5, $t6
				add $t5, $t5, $t6
				addi $t5, $t5, 1
				sw $t5, 16($t0)
				j cont
		cont:	addi $s1, $s1, 1
			j for

				#exit program

	done:
		li $v0, 10
		syscall