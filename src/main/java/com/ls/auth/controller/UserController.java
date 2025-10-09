package com.ls.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
/*    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDto>> list(Pageable p) {
        return ResponseEntity.ok(userService.list(p));
    }

    @GetMapping("/{bidId}")
    public ResponseEntity<UserDto> get(@PathVariable Long bidId) {
        return userService.get(bidId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{bidId}")
    public ResponseEntity<UserDto> update(@PathVariable Long bidId, @Valid @RequestBody UserUpdateDto dto) {
        return ResponseEntity.ok(userService.update(bidId, dto));
    }

    @DeleteMapping("/{bidId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long bidId) {
        userService.delete(bidId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{bidId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> assignRoles(@PathVariable Long bidId, @Valid @RequestBody RoleAssignDto dto) {
        return ResponseEntity.ok(userService.assignRoles(bidId, dto));
    }*/
}
