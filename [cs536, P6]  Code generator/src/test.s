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
	.global main
main:	
#———Entry———————————————
	sw    $ra, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	sw    $fp, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	addu  $fp, $sp, 12
	subu  $sp, $sp, 4
#———Body———————————————
	lw    $v0, 4($sp)	# POP
	addu  $sp, $sp, 4
	j     .L0		# epilogue
#———Exit———————————————
.L0:		# epilogue
	lw    $ra, $fp, -4
	move  $t0, $fp
	lw    $fp, $fp, -8
	move  $sp, $t0
	jr    $ra

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
	lw    $v0, 4($sp)	# POP
	addu  $sp, $sp, 4
	j     .L1		# epilogue
#———Exit———————————————
.L1:		# epilogue
	lw    $ra, $fp, -8
	move  $t0, $fp
	lw    $fp, $fp, -12
	move  $sp, $t0
	li    $v0, 10
	syscall
