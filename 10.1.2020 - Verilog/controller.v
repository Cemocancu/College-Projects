`timescale 1ms / 1ns

module controller(
    input congestion,
    input clock,
    output reg[1:0] main_road,
    output reg[1:0] side_road);
    parameter go = 2'b00, go_attention = 2'b01, stop = 2'b10, stop_attention = 2'b11;
    reg[1:0] next_state, next_state0;
    reg[32:0] sec, congestion_sec;
    integer check_congestion;
    
    initial begin
        sec <= 0;
        congestion_sec <= 0;
        check_congestion <= 0;
    end
      
    always@(posedge clock) begin
        main_road <= next_state;
        side_road <= next_state0;
        sec <= sec + 1;
        if(check_congestion == 1) congestion_sec <= congestion_sec + 1;
     end
     
    always@(negedge clock) begin
            
            if(congestion == 1) check_congestion <= 1;
            
            if(check_congestion == 0) begin
            case(main_road)
                go: if(sec < 20) begin
                    next_state <= go;
                    next_state0 <= stop;
                end else if(sec >= 20) begin
                    next_state <= go_attention;
                    next_state0 <= stop_attention;
                    sec <= 0;
                end go_attention: if(sec < 3) begin
                    next_state <= go_attention;
                    next_state0 <= stop_attention;
                end else if(sec >= 3) begin
                    next_state <= stop;
                    next_state0 <= go;
                    sec <= 0;
                end stop: if(sec < 19) begin
                    if(congestion == 1) begin
                        next_state <= stop_attention;
                        next_state0 <= go_attention;
                        check_congestion <= 1;
                        congestion_sec <= 0;
                    end else if(congestion == 0)begin
                        next_state <= stop;
                        next_state0 <= go;
                    end
                end else if(sec >= 19) begin
                    next_state <= stop_attention;
                    next_state0 <= go_attention;
                    sec <= 0;
                end stop_attention: if(sec < 3) begin
                    next_state <= stop_attention; 
                    next_state0 <= go_attention;
                end else if(sec >= 3) begin
                    next_state <= go;
                    next_state0 <= stop;
                    sec <= 0;
                end default begin
                    next_state <= go;
                    next_state0 <= stop;
                end
             endcase
         end else if(check_congestion == 1) begin
         case(main_road)
            go: if(congestion_sec < 40) begin
                next_state <= go;
                next_state0 <= stop;
            end else if(congestion_sec >= 40) begin
                if(congestion == 1) begin
                    next_state <= go;
                    next_state0 <= stop;
                end else if(congestion == 0) begin
                    check_congestion <= 0;
                    congestion_sec <= 0;
                    sec <= 0;
                    next_state <= go_attention;
                    next_state0 <= stop_attention;
                end
            end
            go_attention: if(congestion_sec < 2) begin
                next_state <= go_attention;
                next_state0 <= stop_attention;
            end else if(congestion_sec >= 2) begin
                next_state <= stop;
                next_state0 <= go;
                congestion_sec <= 0;
            end
            stop: if(congestion_sec < 5) begin
                next_state <= stop;
                next_state0 <= go;
            end else if(congestion_sec >= 5) begin
                next_state <= stop_attention;
                next_state0 <= go_attention;
                congestion_sec <= 0;
            end
            stop_attention: if(congestion_sec < 3) begin
                next_state <= stop_attention;
                next_state0 <= go_attention;
            end else if(congestion_sec >= 3) begin
                next_state <= go;
                next_state0 <= stop;
                congestion_sec <= 0;
            end
            default begin
                next_state <= go;
                next_state0 <= stop;
            end
         endcase
         end
    end
endmodule
