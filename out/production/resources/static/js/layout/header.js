// 쿠키에서 특정 쿠키 값을 읽어오는 함수
    function getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
    }

    // JWT 토큰에서 권한 정보만 추출하는 함수
    function parseJwt(token) {
        var base64Url = token.split('.')[1];
        var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        var jsonPayload = decodeURIComponent(
            atob(base64)
                .split('')
                .map(function (c) {
                    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
                })
                .join('')
        );

        return JSON.parse(jsonPayload).roles;
    }

    // 서버에 새로운 토큰을 요청하는 함수
    function refreshToken() {
        const refreshToken = getCookie('refreshToken');
                console.log('리프레시 토큰:', refreshToken); // 콘솔에 리프레시 토큰 출력

        if (!refreshToken) {
            console.error('리프레시 토큰이 쿠키에 없습니다.');
            alert('로그인 세션이 만료되었습니다. 다시 로그인해 주세요.');
            logout();
            return;
        }

        $.ajax({
            url: '/user/refresh_token',
            type: 'POST',
            data: { refreshToken: refreshToken },
            success: function (data) {
                localStorage.setItem('accessToken', data.accessToken);
                console.log('엑세스 토큰이 성공적으로 갱신되었습니다.');
                alert('엑세스 토큰이 성공적으로 갱신되었습니다.');
                location.reload(); // 알림창에서 "확인"을 누르면 페이지 새로고침
            },
            error: function () {
                console.error('새로운 토큰을 발급받는 데 실패했습니다.');
                alert('새로운 토큰을 발급받는 데 실패했습니다.');
                logout(); // 실패 시 로그아웃 처리
            },
        });
    }

    // 세션 만료 대화 상자 표시 함수
    function showSessionExpiredDialog() {
        var confirmResponse = confirm('세션 유효 시간이 만료되었습니다. 로그인을 연장하시겠습니까?');
        if (confirmResponse) {
            refreshToken(); // 사용자가 세션 연장을 원할 경우 토큰 재발급 요청
        } else {
            logout(); // 사용자가 세션 연장을 원하지 않을 경우 로그아웃 처리
        }
    }

    $(document).ready(function () {
        var token = localStorage.getItem('accessToken');
        if (token) {
            var roles = parseJwt(token);  // 권한 정보 추출
            $.ajax({
                url: '/user/tokenAll',
                type: 'GET',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('Authorization', 'Bearer ' + token);
                },
                success: function (data) {
                    $('#account-info').text(data.userRealName + '님, 반갑습니다.');
                    $('#logout-link, #MyProfile-link, #account-info').show();
                    $('#login-link, #join-link').hide();

                    if (roles.includes('ROLE_ADMIN')) {
                        $('#adminPage-link').show();
                        $('#MyProfile-link').hide();
                    }

                    // auth-link 클래스에 대한 페이지 이동 처리
                    $('.auth-link').each(function () {
                        var link = $(this);
                        link.click(function (e) {
                            e.preventDefault(); // 링크의 기본 동작을 막음
                            var targetUrl = link.attr('href');
                            loadPage(targetUrl, token);
                        });
                    });
                },
                error: function (xhr) {
                    if (xhr.status === 401) {
                        showSessionExpiredDialog();  // 토큰 만료 시 세션 만료 대화 상자 표시
                    } else {
                        console.error('사용자 정보를 가져오는 데 실패했습니다.');
                        $('#login-link, #join-link').show();
                        $('#account-info, #logout-link, #MyProfile-link').hide();
                    }
                },
            });
        } else {
            $('#login-link, #join-link').show();
        }

        window.onpopstate = function (event) {
            if (event.state) {
                loadPage(event.state.url, token, false);
            }
        };
    });

    function loadPage(url, token, pushState = true) {
        $.ajax({
            url: url,
            type: 'GET',
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + token);
            },
            success: function (data) {
                $('body').html(data);
                if (pushState) {
                    history.pushState({ url: url }, '', url);
                }
            },
            error: function (xhr, status, error) {
                console.error('페이지 로드 실패:', error);
                if (xhr.status === 401) {
                    /*alert('접근 권한이 없습니다. 로그인 또는 회원가입 후 이용해 주세요.');*/
                    location.href = '/user/loginform';
                }
            }
        });
    }

    function logout() {
        localStorage.removeItem('accessToken'); // 로컬 스토리지에서 토큰 제거
        document.cookie = 'refreshToken=;expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;'; // 쿠키에서 리프레시 토큰 제거
        alert('로그아웃 되었습니다.');
        location.href = '/user/Logout';
    }

document.addEventListener('DOMContentLoaded', function() {
    // 이벤트 위임을 사용하여 mouseover와 mouseout 이벤트 처리 (햄버거 메뉴가 활성화되지 않은 경우에만)
    function handleMouseOver(event) {
        if (window.innerWidth > 1330) {
            var parentMenuItem = event.target.closest('.p-nav-sub__parent > li');
            if (parentMenuItem) {
                var childMenu = parentMenuItem.querySelector('.p-nav-sub__child');
                if (childMenu && childMenu.children.length > 0) {
                    childMenu.style.display = 'block';
                }
            }
        }
    }

    function handleMouseOut(event) {
        if (window.innerWidth > 1330) {
            var parentMenuItem = event.target.closest('.p-nav-sub__parent > li');
            if (parentMenuItem) {
                var childMenu = parentMenuItem.querySelector('.p-nav-sub__child');
                if (childMenu) {
                    childMenu.style.display = 'none';
                }
            }
        }
    }

    document.querySelector('.p-nav-global').addEventListener('mouseover', handleMouseOver);
    document.querySelector('.p-nav-global').addEventListener('mouseout', handleMouseOut);

    // 초기 로드 시 하위 메뉴가 비어 있으면 숨기기
    var subMenus = document.querySelectorAll('.p-nav-sub__child');
    subMenus.forEach(function(menu) {
        if (menu.children.length === 0) {
            menu.style.display = 'none';
        }
    });

    // 햄버거 메뉴 클릭 이벤트
    $('#hamburger-menu').click(function() {
        var navSub = $('#nav-sub');
        var utils = $('.utils');

        if (navSub.is(':visible')) {
            navSub.removeClass('slide-down').addClass('slide-up');
            utils.removeClass('slide-down').addClass('slide-up');
            setTimeout(function() {
                navSub.hide();
                utils.hide();
            }, 300); // 슬라이드 업 애니메이션 시간과 동일하게 설정
        } else {
            navSub.removeClass('slide-up').addClass('slide-down').show();
            utils.removeClass('slide-up').addClass('slide-down').show();
        }
    });

    // 서브 메뉴 토글 (햄버거 메뉴일 때만 적용)
    $(document).on('click', '.hamburger-menu + .p-nav-global .p-nav-sub__parent > li > a', function(e) {
        var submenu = $(this).next('.p-nav-sub__child');
        if (submenu.length) {
            e.preventDefault(); // 링크 클릭 기본 동작 방지
            submenu.slideToggle();
        }
    });

    // 화면 크기 변경 시 이벤트 처리
    window.addEventListener('resize', function() {
        var width = window.innerWidth;
        if (width > 1330) {
            $('#nav-sub').removeClass('slide-down slide-up').show();
            $('.utils').removeClass('slide-down slide-up').show();
        } else {
            $('#nav-sub').hide();
            $('.utils').hide();
        }
    });

    // 페이지 로드 시 화면 크기 체크
    var width = window.innerWidth;
    if (width > 1330) {
        $('#nav-sub').removeClass('slide-down slide-up').show();
        $('.utils').removeClass('slide-down slide-up').show();
    } else {
        $('#nav-sub').hide();
        $('.utils').hide();
    }
});
