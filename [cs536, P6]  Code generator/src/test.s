	.data
	.align 2		# align on a word boundary
_x:	.space 4
	.data
	.align 2		# align on a word boundary
_y:	.space 4

########################
# ⨍	main
########################
	.text
	.globl main
main:	
#———Entry———————————————
	sw    $ra, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	sw    $fp, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	addu  $fp, $sp, 12
	subu  $sp, $sp, 4
#———Body———————————————
	b     .L1
.L1:		# case: true
	li    $t0, 1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $v0, 4($sp)	# POP
	addu  $sp, $sp, 4
	b     .L0		# jump: epilogue
.L2:		# case: false
	li    $t0, 0
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $v0, 4($sp)	# POP
	addu  $sp, $sp, 4
	b     .L0		# jump: epilogue
#———Exit———————————————
.L0:		# epilogue
	lw    $ra, -4($fp)
	move  $t0, $fp
	lw    $fp, -8($fp)
	move  $sp, $t0
	li    $v0, 10		# exit
	syscall

########################
# ⨍	f
########################
	.text
_f:	
#———Entry———————————————
	sw    $ra, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	sw    $fp, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	addu  $fp, $sp, 16
	subu  $sp, $sp, 8
#———Body———————————————
	b     .L4
.L4:		# case: true
	li    $t0, 1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $v0, 4($sp)	# POP
	addu  $sp, $sp, 4
	b     .L3		# jump: epilogue
.L5:		# case: false
	li    $t0, 0
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $v0, 4($sp)	# POP
	addu  $sp, $sp, 4
	b     .L3		# jump: epilogue
#———Exit———————————————
.L3:		# epilogue
	lw    $ra, -8($fp)
	move  $t0, $fp
	lw    $fp, -12($fp)
	move  $sp, $t0
	jr    $ra
