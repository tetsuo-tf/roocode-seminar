// ToDo App JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Initialize Material Design components
    initializeMDL();

    // Initialize app features
    initializeAutoHideMessages();
    initializeFormValidation();
    initializeConfirmDialogs();
    initializeDatePickers();
    initializeSearchFilters();
    initializePagination();
});

/**
 * Initialize Material Design Lite components
 */
function initializeMDL() {
    // Upgrade all MDL components
    if (typeof componentHandler !== 'undefined') {
        componentHandler.upgradeAllRegistered();
    }
}

/**
 * Auto-hide success/error messages after 5 seconds
 */
function initializeAutoHideMessages() {
    const messages = document.querySelectorAll('.message-card');
    messages.forEach(function(message) {
        setTimeout(function() {
            message.style.transition = 'opacity 0.5s ease-out';
            message.style.opacity = '0';
            setTimeout(function() {
                if (message.parentNode) {
                    message.parentNode.removeChild(message);
                }
            }, 500);
        }, 5000);
    });
}

/**
 * Initialize form validation
 */
function initializeFormValidation() {
    const forms = document.querySelectorAll('form');
    forms.forEach(function(form) {
        form.addEventListener('submit', function(e) {
            if (!validateForm(form)) {
                e.preventDefault();
                return false;
            }
        });
    });
}

/**
 * Validate form fields
 */
function validateForm(form) {
    let isValid = true;

    // Validate required fields
    const requiredFields = form.querySelectorAll('[required]');
    requiredFields.forEach(function(field) {
        if (!field.value.trim()) {
            showFieldError(field, 'この項目は必須です');
            isValid = false;
        } else {
            clearFieldError(field);
        }
    });

    // Validate email fields
    const emailFields = form.querySelectorAll('input[type="email"]');
    emailFields.forEach(function(field) {
        if (field.value && !isValidEmail(field.value)) {
            showFieldError(field, '有効なメールアドレスを入力してください');
            isValid = false;
        }
    });

    // Validate password confirmation
    const passwordField = form.querySelector('input[name="password"]');
    const confirmPasswordField = form.querySelector('input[name="confirmPassword"]');
    if (passwordField && confirmPasswordField) {
        if (passwordField.value !== confirmPasswordField.value) {
            showFieldError(confirmPasswordField, 'パスワードが一致しません');
            isValid = false;
        }
    }

    // Validate due date
    const dueDateField = form.querySelector('input[name="dueDate"]');
    if (dueDateField && dueDateField.value) {
        const selectedDate = new Date(dueDateField.value);
        const today = new Date();
        today.setHours(0, 0, 0, 0);

        if (selectedDate < today) {
            showFieldError(dueDateField, '期限日は今日以降の日付を設定してください');
            isValid = false;
        }
    }

    return isValid;
}

/**
 * Show field error message
 */
function showFieldError(field, message) {
    const textfield = field.closest('.mdl-textfield');
    if (textfield) {
        textfield.classList.add('is-invalid');
        let errorElement = textfield.querySelector('.mdl-textfield__error');
        if (errorElement) {
            errorElement.textContent = message;
        }
    }
}

/**
 * Clear field error message
 */
function clearFieldError(field) {
    const textfield = field.closest('.mdl-textfield');
    if (textfield) {
        textfield.classList.remove('is-invalid');
    }
}

/**
 * Validate email format
 */
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

/**
 * Initialize confirmation dialogs
 */
function initializeConfirmDialogs() {
    const deleteButtons = document.querySelectorAll('[data-confirm]');
    deleteButtons.forEach(function(button) {
        button.addEventListener('click', function(e) {
            const message = button.getAttribute('data-confirm');
            if (!confirm(message)) {
                e.preventDefault();
                return false;
            }
        });
    });
}

/**
 * Initialize date pickers
 */
function initializeDatePickers() {
    const dateFields = document.querySelectorAll('input[type="date"]');
    dateFields.forEach(function(field) {
        // Set minimum date to today
        const today = new Date().toISOString().split('T')[0];
        field.setAttribute('min', today);
    });
}

/**
 * Initialize search and filter functionality
 */
function initializeSearchFilters() {
    const searchForm = document.querySelector('#searchForm');
    if (searchForm) {
        const searchInput = searchForm.querySelector('input[name="search"]');
        const filterSelect = searchForm.querySelector('select[name="completed"]');

        // Auto-submit on filter change
        if (filterSelect) {
            filterSelect.addEventListener('change', function() {
                searchForm.submit();
            });
        }

        // Clear search functionality
        const clearButton = document.querySelector('#clearSearch');
        if (clearButton) {
            clearButton.addEventListener('click', function() {
                if (searchInput) searchInput.value = '';
                if (filterSelect) filterSelect.value = '';
                searchForm.submit();
            });
        }
    }
}

/**
 * Initialize pagination
 */
function initializePagination() {
    const paginationLinks = document.querySelectorAll('.pagination-link');
    paginationLinks.forEach(function(link) {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const url = link.getAttribute('href');
            if (url) {
                window.location.href = url;
            }
        });
    });
}

/**
 * Toggle todo completion status
 */
function toggleTodoCompletion(todoId) {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = `/todos/${todoId}/toggle`;

    // Add CSRF token if available
    const csrfToken = document.querySelector('meta[name="_csrf"]');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]');
    if (csrfToken && csrfHeader) {
        const csrfInput = document.createElement('input');
        csrfInput.type = 'hidden';
        csrfInput.name = csrfHeader.getAttribute('content');
        csrfInput.value = csrfToken.getAttribute('content');
        form.appendChild(csrfInput);
    }

    document.body.appendChild(form);
    form.submit();
}

/**
 * Show loading state
 */
function showLoading(element) {
    if (element) {
        element.classList.add('loading');
        element.setAttribute('disabled', 'disabled');
    }
}

/**
 * Hide loading state
 */
function hideLoading(element) {
    if (element) {
        element.classList.remove('loading');
        element.removeAttribute('disabled');
    }
}

/**
 * Show toast message
 */
function showToast(message, type = 'info') {
    const toast = document.createElement('div');
    toast.className = `mdl-card mdl-shadow--2dp message-card ${type}-message fade-in`;
    toast.innerHTML = `
        <div class="mdl-card__supporting-text">
            <i class="material-icons">${getToastIcon(type)}</i>
            <span>${message}</span>
        </div>
    `;

    const container = document.querySelector('.page-content');
    if (container) {
        container.insertBefore(toast, container.firstChild);

        // Auto-hide after 3 seconds
        setTimeout(function() {
            toast.style.transition = 'opacity 0.5s ease-out';
            toast.style.opacity = '0';
            setTimeout(function() {
                if (toast.parentNode) {
                    toast.parentNode.removeChild(toast);
                }
            }, 500);
        }, 3000);
    }
}

/**
 * Get icon for toast type
 */
function getToastIcon(type) {
    switch (type) {
        case 'success': return 'check_circle';
        case 'error': return 'error';
        case 'warning': return 'warning';
        default: return 'info';
    }
}

/**
 * Format date for display
 */
function formatDate(dateString) {
    if (!dateString) return '';

    const date = new Date(dateString);
    const today = new Date();
    const tomorrow = new Date(today);
    tomorrow.setDate(tomorrow.getDate() + 1);

    // Reset time for comparison
    today.setHours(0, 0, 0, 0);
    tomorrow.setHours(0, 0, 0, 0);
    date.setHours(0, 0, 0, 0);

    if (date.getTime() === today.getTime()) {
        return '今日';
    } else if (date.getTime() === tomorrow.getTime()) {
        return '明日';
    } else {
        return date.toLocaleDateString('ja-JP');
    }
}

/**
 * Get relative time string
 */
function getRelativeTime(dateString) {
    if (!dateString) return '';

    const date = new Date(dateString);
    const now = new Date();
    const diffMs = date.getTime() - now.getTime();
    const diffDays = Math.ceil(diffMs / (1000 * 60 * 60 * 24));

    if (diffDays < 0) {
        return `${Math.abs(diffDays)}日前`;
    } else if (diffDays === 0) {
        return '今日';
    } else if (diffDays === 1) {
        return '明日';
    } else {
        return `${diffDays}日後`;
    }
}

/**
 * Debounce function for search input
 */
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

/**
 * Handle keyboard shortcuts
 */
document.addEventListener('keydown', function(e) {
    // Ctrl/Cmd + N: New todo
    if ((e.ctrlKey || e.metaKey) && e.key === 'n') {
        e.preventDefault();
        const newTodoLink = document.querySelector('a[href*="/todos/new"]');
        if (newTodoLink) {
            window.location.href = newTodoLink.href;
        }
    }

    // Escape: Close modals or clear search
    if (e.key === 'Escape') {
        const searchInput = document.querySelector('input[name="search"]');
        if (searchInput && searchInput === document.activeElement) {
            searchInput.value = '';
            searchInput.blur();
        }
    }
});

/**
 * Handle window resize for responsive design
 */
window.addEventListener('resize', debounce(function() {
    // Reinitialize MDL components if needed
    if (typeof componentHandler !== 'undefined') {
        componentHandler.upgradeAllRegistered();
    }
}, 250));

/**
 * Handle page visibility change
 */
document.addEventListener('visibilitychange', function() {
    if (!document.hidden) {
        // Page became visible, refresh timestamps
        updateRelativeTimes();
    }
});

/**
 * Update relative time displays
 */
function updateRelativeTimes() {
    const timeElements = document.querySelectorAll('[data-time]');
    timeElements.forEach(function(element) {
        const dateString = element.getAttribute('data-time');
        element.textContent = getRelativeTime(dateString);
    });
}

// Update relative times every minute
setInterval(updateRelativeTimes, 60000);

// Export functions for global use
window.TodoApp = {
    toggleTodoCompletion,
    showToast,
    formatDate,
    getRelativeTime,
    showLoading,
    hideLoading
};
