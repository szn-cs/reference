	.data
	.align 2		# align on a word boundary
_numValidInputs:	.space 4

########################
# ⨍	multiplyTwoNumbers
########################
	.text
_multiplyTwoNumbers:	
#———Entry———————————————
	sw    $ra, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	sw    $fp, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	addu  $fp, $sp, 16
	subu  $sp, $sp, 12
#———Body———————————————
	lw    $t0, 0($fp)	# variable: num1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, -4($fp)	# variable: num2
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	mulo  $t0, $t0, $t1		# arithmetic
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	la    $t0, -16($fp)	# variable: result1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	sw    $t1, 0($t0)	# assign to address
	sw    $t1, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, -4($fp)	# variable: num2
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 0($fp)	# variable: num1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	mulo  $t0, $t0, $t1		# arithmetic
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	la    $t0, -20($fp)	# variable: result2
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	sw    $t1, 0($t0)	# assign to address
	sw    $t1, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, -16($fp)	# variable: result1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, -20($fp)	# variable: result2
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	seq   $t0, $t0, $t1		# equality
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	beq   $t0, 0, .L2
	b     .L5
.L5:
	lw    $t0, -16($fp)	# variable: result1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, -20($fp)	# variable: result2
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	sle   $t0, $t0, $t1		# relational
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	beq   $t0, 0, .L2
	b     .L4
.L4:
	lw    $t0, -16($fp)	# variable: result1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, -20($fp)	# variable: result2
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	sge   $t0, $t0, $t1		# relational
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	beq   $t0, 0, .L2
	b     .L1
.L1:		# case: true
	lw    $t0, -20($fp)	# variable: result2
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, -16($fp)	# variable: result1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	seq   $t0, $t0, $t1		# equality
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	beq   $t0, 0, .L7
	b     .L10
.L10:
	lw    $t0, -20($fp)	# variable: result2
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, -16($fp)	# variable: result1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	sle   $t0, $t0, $t1		# relational
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	beq   $t0, 0, .L7
	b     .L9
.L9:
	lw    $t0, -20($fp)	# variable: result2
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, -16($fp)	# variable: result1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	sge   $t0, $t0, $t1		# relational
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	beq   $t0, 0, .L7
	b     .L6
.L6:		# case: true
	lw    $t0, -16($fp)	# variable: result1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, -16($fp)	# variable: result1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	seq   $t0, $t0, $t1		# equality
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	beq   $t0, 0, .L14
	b     .L11
.L14:
	lw    $t0, -24($fp)	# variable: undefVar
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	li    $t0, 1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	sub   $t0, $zero, $t0		# negate value
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	slt   $t0, $t0, $t1		# relational
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	beq   $t0, 0, .L12
	b     .L11
.L11:		# case: true
	lw    $t0, -16($fp)	# variable: result1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $v0, 4($sp)	# POP
	addu  $sp, $sp, 4
	b     .L0		# jump: epilogue
	b     .L13		# jump: done
.L12:		# case: false
	li    $t0, 1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	sub   $t0, $zero, $t0		# negate value
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $v0, 4($sp)	# POP
	addu  $sp, $sp, 4
	b     .L0		# jump: epilogue
.L13:		# done branching
	b     .L8		# jump: done
.L7:		# case: false
	li    $t0, 1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	sub   $t0, $zero, $t0		# negate value
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $v0, 4($sp)	# POP
	addu  $sp, $sp, 4
	b     .L0		# jump: epilogue
.L8:		# done branching
	b     .L3		# jump: done
.L2:		# case: false
	li    $t0, 1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	sub   $t0, $zero, $t0		# negate value
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $v0, 4($sp)	# POP
	addu  $sp, $sp, 4
	b     .L0		# jump: epilogue
.L3:		# done branching
#———Exit———————————————
.L0:		# epilogue
	lw    $ra, $fp, -8
	move  $t0, $fp
	lw    $fp, $fp, -12
	move  $sp, $t0
	li    $v0, 10
	syscall

########################
# ⨍	doFactorialRecur
########################
	.text
_doFactorialRecur:	
#———Entry———————————————
	sw    $ra, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	sw    $fp, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	addu  $fp, $sp, 12
	subu  $sp, $sp, 0
#———Body———————————————
	lw    $t0, 0($fp)	# variable: num
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	li    $t0, 1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	sle   $t0, $t0, $t1		# relational
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	beq   $t0, 0, .L17
	b     .L16
.L16:		# case: true
	li    $t0, 1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $v0, 4($sp)	# POP
	addu  $sp, $sp, 4
	b     .L15		# jump: epilogue
	b     .L18		# jump: done
.L17:		# case: false
	lw    $t0, 0($fp)	# variable: num
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 0($fp)	# variable: num
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	li    $t0, 1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	sub   $t0, $t0, $t1		# arithmetic
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	jal   doFactorialRecur		# call
	sw    $v0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	jal   multiplyTwoNumbers		# call
	sw    $v0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $v0, 4($sp)	# POP
	addu  $sp, $sp, 4
	b     .L15		# jump: epilogue
.L18:		# done branching
	li    $t0, 1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	sub   $t0, $zero, $t0		# negate value
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $v0, 4($sp)	# POP
	addu  $sp, $sp, 4
	b     .L15		# jump: epilogue
#———Exit———————————————
.L15:		# epilogue
	lw    $ra, $fp, -4
	move  $t0, $fp
	lw    $fp, $fp, -8
	move  $sp, $t0
	li    $v0, 10
	syscall

########################
# ⨍	doFactorialItr
########################
	.text
_doFactorialItr:	
#———Entry———————————————
	sw    $ra, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	sw    $fp, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	addu  $fp, $sp, 12
	subu  $sp, $sp, 8
#———Body———————————————
	li    $t0, 1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	la    $t0, -12($fp)	# variable: i
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	sw    $t1, 0($t0)	# assign to address
	sw    $t1, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $t0, 1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	la    $t0, -16($fp)	# variable: prod
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	sw    $t1, 0($t0)	# assign to address
	sw    $t1, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
.L20:		# while block:
	lw    $t0, -12($fp)	# variable: i
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 0($fp)	# variable: num
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	slt   $t0, $t0, $t1		# relational
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	beq   $t0, 0, .L22
	b     .L21
.L21:		# case: true
	lw    $t0, -12($fp)	# variable: i
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	li    $t0, 1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	add   $t0, $t0, $t1		# arithmetic
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	la    $t0, -12($fp)	# variable: i
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	sw    $t1, 0($t0)	# assign to address
	sw    $t1, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, -16($fp)	# variable: prod
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, -12($fp)	# variable: i
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	jal   multiplyTwoNumbers		# call
	sw    $v0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	la    $t0, -16($fp)	# variable: prod
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	sw    $t1, 0($t0)	# assign to address
	sw    $t1, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	b     .L20		# jump back: while
.L22:		# case: false
	lw    $t0, -16($fp)	# variable: prod
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $v0, 4($sp)	# POP
	addu  $sp, $sp, 4
	b     .L19		# jump: epilogue
#———Exit———————————————
.L19:		# epilogue
	lw    $ra, $fp, -4
	move  $t0, $fp
	lw    $fp, $fp, -8
	move  $sp, $t0
	li    $v0, 10
	syscall

########################
# ⨍	isDone
########################
	.text
_isDone:	
#———Entry———————————————
	sw    $ra, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	sw    $fp, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	addu  $fp, $sp, 12
	subu  $sp, $sp, 4
#———Body———————————————
	lw    $t0, 0($fp)	# variable: num
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	li    $t0, 1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	sub   $t0, $zero, $t0		# negate value
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	seq   $t0, $t0, $t1		# equality
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	la    $t0, -12($fp)	# variable: finished
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	sw    $t1, 0($t0)	# assign to address
	sw    $t1, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $t0, 999
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	sub   $t0, $zero, $t0		# negate value
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	la    $t0, 0($fp)	# variable: num
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	sw    $t1, 0($t0)	# assign to address
	sw    $t1, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, -12($fp)	# variable: finished
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $v0, 4($sp)	# POP
	addu  $sp, $sp, 4
	b     .L23		# jump: epilogue
#———Exit———————————————
.L23:		# epilogue
	lw    $ra, $fp, -4
	move  $t0, $fp
	lw    $fp, $fp, -8
	move  $sp, $t0
	li    $v0, 10
	syscall

########################
# ⨍	loopTilDone
########################
	.text
_loopTilDone:	
#———Entry———————————————
	sw    $ra, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	sw    $fp, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	addu  $fp, $sp, 8
	subu  $sp, $sp, 12
#———Body———————————————
	.data
.L25:	.asciiz "("	# string
	.text
	la    $t0, .L25
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $v0, 4
	syscall		# write
	lw    $t0, _numValidInputs
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $v0, 1
	syscall		# write
	.data
.L26:	.asciiz ") "	# string
	.text
	la    $t0, .L26
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $v0, 4
	syscall		# write
	.data
.L27:	.asciiz "Enter a number to take the factorial of, -1 to exit: "	# string
	.text
	la    $t0, .L27
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $v0, 4
	syscall		# write
	li    $v0, 5		# read integer
	syscall
	la    $t0, -8($fp)	# variable: num
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	sw    $v0, 0($t0)
.L28:		# while block:
	lw    $t0, -8($fp)	# variable: num
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	jal   isDone		# call
	sw    $v0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	beq   $t0, 0, .L29
	b     .L30
.L29:		# case: true
	lw    $t0, -8($fp)	# variable: num
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	li    $t0, 1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	sub   $t0, $zero, $t0		# negate value
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	slt   $t0, $t0, $t1		# relational
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	beq   $t0, 0, .L32
	b     .L31
.L31:		# case: true
	.data
.L33:	.asciiz "You entered a number less than -1, please try a different number.\n"	# string
	.text
	la    $t0, .L33
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $v0, 4
	syscall		# write
	jal   loopTilDone		# call
	sw    $v0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	b     .L24		# jump: epilogue
.L32:		# case: false
	lw    $t0, -8($fp)	# variable: num
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	jal   doFactorialRecur		# call
	sw    $v0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	la    $t0, -12($fp)	# variable: recurAns
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	sw    $t1, 0($t0)	# assign to address
	sw    $t1, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, -8($fp)	# variable: num
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	jal   doFactorialItr		# call
	sw    $v0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	la    $t0, -16($fp)	# variable: itrAns
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	sw    $t1, 0($t0)	# assign to address
	sw    $t1, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	.data
.L34:	.asciiz "The recursive answer is "	# string
	.text
	la    $t0, .L34
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $v0, 4
	syscall		# write
	lw    $t0, -12($fp)	# variable: recurAns
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $v0, 1
	syscall		# write
	.data
.L35:	.asciiz ".\n"	# string
	.text
	la    $t0, .L35
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $v0, 4
	syscall		# write
	.data
.L36:	.asciiz "The iterative answer is "	# string
	.text
	la    $t0, .L36
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $v0, 4
	syscall		# write
	lw    $t0, -16($fp)	# variable: itrAns
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $v0, 1
	syscall		# write
	la    $t0, .L35
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $v0, 4
	syscall		# write
	lw    $t0, -12($fp)	# variable: recurAns
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, -16($fp)	# variable: itrAns
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	seq   $t0, $t0, $t1		# equality
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	beq   $t0, 0, .L38
	b     .L37
.L37:		# case: true
	.data
.L40:	.asciiz "These answers match.\n"	# string
	.text
	la    $t0, .L40
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $v0, 4
	syscall		# write
	b     .L39		# jump: done
.L38:		# case: false
	.data
.L41:	.asciiz "These answers do NOT match.\n"	# string
	.text
	la    $t0, .L41
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $v0, 4
	syscall		# write
.L39:		# done branching
	lw    $t0, _numValidInputs
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	li    $t0, 1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	add   $t0, $t0, $t1		# arithmetic
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	la    $t0, _numValidInputs
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	sw    $t1, 0($t0)	# assign to address
	sw    $t1, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	la    $t0, .L25
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $v0, 4
	syscall		# write
	lw    $t0, _numValidInputs
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $v0, 1
	syscall		# write
	la    $t0, .L26
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $v0, 4
	syscall		# write
	la    $t0, .L27
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $v0, 4
	syscall		# write
	li    $v0, 5		# read integer
	syscall
	la    $t0, -8($fp)	# variable: num
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	sw    $v0, 0($t0)
	b     .L28		# jump back: while
.L30:		# case: false
#———Exit———————————————
.L24:		# epilogue
	lw    $ra, $fp, 0
	move  $t0, $fp
	lw    $fp, $fp, -4
	move  $sp, $t0
	li    $v0, 10
	syscall

########################
# ⨍	main
########################
	.text
	.global main
main:	
#———Entry———————————————
	sw    $ra, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	sw    $fp, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	addu  $fp, $sp, 8
	subu  $sp, $sp, 0
#———Body———————————————
	li    $t0, 1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	la    $t0, _numValidInputs
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	sw    $t1, 0($t0)	# assign to address
	sw    $t1, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	.data
.L43:	.asciiz "Program is starting...\n"	# string
	.text
	la    $t0, .L43
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $v0, 4
	syscall		# write
	jal   loopTilDone		# call
	sw    $v0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	.data
.L44:	.asciiz "Program is terminating...\n"	# string
	.text
	la    $t0, .L44
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $v0, 4
	syscall		# write
	b     .L42		# jump: epilogue
#———Exit———————————————
.L42:		# epilogue
	lw    $ra, $fp, 0
	move  $t0, $fp
	lw    $fp, $fp, -4
	move  $sp, $t0
	jr    $ra
